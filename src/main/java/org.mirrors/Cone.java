package org.mirrors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.*;

public class Cone extends Shape {
    public float radius;
    public float height;

    public Cone() {
        super();
        this.radius = 1.f;
        this.height = 1.f;
    }

    public Cone(float radius, float height) {
        super();
        this.radius = radius;
        this.height = height;
    }

    public Cone(Transformation transformation, float radius, float height) {
        super(transformation);
        this.radius = radius;
        this.height = height;
    }

    public Cone(Transformation transformation) {
        super(transformation);
        this.radius = 1.f;
        this.height = 1.f;
    }

    public Cone(Material material) {
        super(material);
        this.radius = 1.f;
        this.height = 1.f;
    }
    public Cone(Transformation transformation, Material material) {
        super(transformation, material);
        this.radius = 1.f;
        this.height = 1.f;
    }

    public Cone(Transformation transformation, Material material, float radius, float height) {
        super(transformation, material);
        this.radius = radius;
        this.height = height;
    }

    public HitRecord rayIntersection(Ray ray) {
        List<HitRecord> rayIntersectionList = this.rayIntersectionList(ray);
        return rayIntersectionList.size() == 0 ? null : rayIntersectionList.get(0);
    }

    public List<HitRecord> rayIntersectionList(Ray ray) {
        List<HitRecord> hits = new ArrayList<>();
        Ray invRay = ray.transform(this.transformation.inverse());

        Plain planeBottom = new Plain();
        HitRecord hitBottom = planeBottom.rayIntersection(invRay);

        if (hitBottom != null) {
            Point pointInt = hitBottom.worldPoint;
            if (pointInt.x * pointInt.x + pointInt.y * pointInt.y < this.radius * this.radius)
                hits.add(new HitRecord(
                        (Point) this.transformation.times(pointInt),
                        (Normal) this.transformation.times(this.getNormal(pointInt, ray.dir)),
                        this.conePointToUV(pointInt),
                        hitBottom.t,
                        ray,
                        this)
                );
        }

        float dx = invRay.dir.x;
        float dy = invRay.dir.y;
        float dz = invRay.dir.z;

        float ox = invRay.origin.x;
        float oy = invRay.origin.y;
        float oz = invRay.origin.z;

        float hr = (float) Math.pow((this.height / this.radius), 2f);

        float a = hr * (float) (Math.pow(dx, 2f) + Math.pow(dy, 2f)) - (float) Math.pow(dz, 2f);
        float b = 2f * (hr * (ox * dx + oy * dy) - dz * oz + this.height * dz);
        float c = hr * (float) (Math.pow(ox, 2f) + Math.pow(oy, 2f)) + 2f * oz * this.height - (float) Math.pow(oz, 2f) - (float) Math.pow(this.height, 2f);

        float delta = b * b - 4.0f * a * c;
        if (delta <= 0.0f) {
            return hits;
        }

        float sqrtDelta = (float) Math.sqrt(delta);
        float tMin;
        float tmax;
        if (abs(a) < 1e-4f) {
            tMin = -c / b;
            tmax = Float.POSITIVE_INFINITY;
        } else {
            tMin = (-b - sqrtDelta) / (2.0f * a);
            tmax = (-b + sqrtDelta) / (2.0f * a);
        }

        if (tMin > invRay.tMin && tMin < invRay.tMax) {
            Point hitPoint = invRay.at(tMin);
            if (hitPoint.z >= 0f && hitPoint.z <= this.height) {
                hits.add(new HitRecord(
                        (Point) this.transformation.times(hitPoint),
                        (Normal) this.transformation.times(getNormal(hitPoint, ray.dir)),
                        conePointToUV(hitPoint),
                        tMin,
                        ray,
                        this));
            }
        }
        if (tmax > invRay.tMin && tmax < invRay.tMax) {
            Point hitPoint = invRay.at(tmax);
            if (hitPoint.z >= 0f && hitPoint.z <= this.height) {
                hits.add(new HitRecord(
                        (Point) this.transformation.times(hitPoint),
                        (Normal) this.transformation.times(getNormal(hitPoint, ray.dir)),
                        conePointToUV(hitPoint),
                        tmax,
                        ray,
                        this));
            }
        }

        if (!hits.isEmpty()) {
            Collections.sort(hits, Comparator.comparingDouble(h -> h.t));
        }
        return hits;
    }

    @Override
    public boolean isInternal(Point a) {
        Point invPoint = (Point) this.transformation.inverse().times(a);
        float x = invPoint.x * this.height / this.radius;
        float y = invPoint.y * this.height / this.radius;
        float z = invPoint.z - this.height;
        if (z * z >= x * x + y * y && this.conePointToUV(invPoint).v < this.height)
            return true;
        else
            return false;
    }

    private Normal getNormal(Point point, Vec rayDir) {
        Normal result;
        if (abs(point.z) < 1e-4f) {
            result = new Normal(0f, 0f, -1.0f);
        } else {
            float rad = (float) Math.sqrt(point.x * point.x + point.y * point.y);
            result = new Normal(point.x / rad, point.y / rad, this.radius / this.height);
            result.normalize();
        }
        if (result.toVec().dot(rayDir) > 0.0f)
            result = (Normal) result.neg();
        return result;
    }

    private Vec2d conePointToUV(Point point) {
        float u = 0f, v = 0f;
        if (abs(point.z) <1e-4f) {
            u = (point.x + this.radius) / (4f * this.radius);  // u in [0,0.5]
            v = (point.y + this.radius) / (4f * this.radius);  // v in [0,0.5]
        } else {
            u = (float) (((Math.atan2(point.y, point.x) + (2f * PI)) % (2f * PI)) / (2.0f * PI));
            u = 0.5f + (u / 2f);
            v = (point.z) / this.height;
        }
        return new Vec2d(u, v);
    }
}

    /*
    public Cone() {
        super();
    }
    public Cone(Transformation transformation) {
        super(transformation);
    }
    public Cone(Material material) {
        super(material);
    }
    public Cone(Transformation transformation, Material material) {
        super(transformation, material);
    }

    @Override
    public HitRecord rayIntersection(Ray ray) {
        Ray invRay = ray.transform(this.transformation.inverse());
        float a = invRay.dir.x * invRay.dir.x + invRay.dir.y * invRay.dir.y - invRay.dir.z * invRay.dir.z;
        float b = 2.0f * (invRay.dir.x * invRay.origin.x + invRay.dir.y * invRay.origin.y - invRay.dir.z * invRay.origin.z);
        float c = invRay.origin.x * invRay.origin.x + invRay.origin.y * invRay.origin.y - invRay.origin.z * invRay.origin.z;

        float delta = b * b - 4.0f * a * c;
        if (delta <= 0.0f) {
            return null;
        }
        float sqrtDelta = (float) Math.sqrt(delta);

        float tMin = (-b - sqrtDelta) / (2.0f * a);
        float tMax = (-b + sqrtDelta) / (2.0f * a);
        float firstHitT;

        if (tMin < invRay.tMax && tMin > invRay.tMin && invRay.at(tMin).z > 0.0f && invRay.at(tMin).z < 1.0f) {
            firstHitT = tMin;
        } else if (tMax < invRay.tMax && tMax > invRay.tMin && invRay.at(tMax).z > 0.0f && invRay.at(tMax).z < 1.0f) {
            firstHitT = tMax;
        } else {
            return null;
        }

        Point hitPoint = invRay.at(firstHitT);

        float phi = (float) Math.atan2(hitPoint.y, hitPoint.x);
        if (phi < 0) {
            phi += 2.0f * (float) Math.PI;
        }
        float u = phi / (2.0f * (float) Math.PI);
        float v = hitPoint.z;

        var normal = new Normal(hitPoint.x, hitPoint.y, -hitPoint.z);
        if (normal.toVec().dot(invRay.dir) > 0.0f) {
            normal = (Normal) normal.neg();
        }

        return new HitRecord(
                (Point) this.transformation.times(hitPoint),
                (Normal) this.transformation.times(normal),
                new Vec2d(u, v),
                firstHitT,
                ray,
                this
        );
    }

    @Override
    public List<HitRecord> rayIntersectionList(Ray ray) {
        return null;
    }

    @Override
    public boolean isInternal(Point point) {
        return false;
    }
}*/
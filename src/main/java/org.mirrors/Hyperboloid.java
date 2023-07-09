package org.mirrors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Hyperboloid extends Shape {
    /*
    private float a; // Parametro che influenza la dimensione del raggio dell'iperboloide lungo l'asse x
    private float b; // Parametro che influenza la dimensione del raggio dell'iperboloide lungo l'asse y
    private float c; // Parametro che influenza la dimensione del raggio dell'iperboloide lungo l'asse z
    */
    private float minZ = -2.5f;
    private float maxZ = 2.5f;

    public Hyperboloid(Transformation transformation, Material material) {
        super(transformation, material);
        this.maxZ = 0.5f;
        this.minZ = -0.5f;
    }

    public Hyperboloid(Transformation transformation, Material material, float minZ, float maxZ) {
        super(transformation, material);
        this.maxZ = maxZ;
        this.minZ = minZ;
    }

    /*
    public Hyperboloid(Transformation transformation, Material material) {
        super(transformation, material);
        this.a = 1.0f;
        this.b = 1.0f;
        this.c = 1.0f;
    }

    public Hyperboloid(Transformation transformation, Material material, float a, float b, float c) {
        super(transformation, material);
        this.a = a;
        this.b = b;
        this.c = c;
    }
    */

/*
    @Override
    public HitRecord rayIntersection(Ray ray) {
        Ray invRay = ray.transform(this.transformation.inverse());
        float a = this.a * (invRay.dir.x * invRay.dir.x + invRay.dir.y * invRay.dir.y) - this.c * invRay.dir.z * invRay.dir.z;
        float b = 2.f * (this.a * (invRay.dir.x * invRay.origin.x + invRay.dir.y * invRay.origin.y) - this.c * invRay.dir.z * invRay.origin.z);
        float c = this.a * (invRay.origin.x * invRay.origin.x + invRay.origin.y * invRay.origin.y) - this.c * invRay.origin.z * invRay.origin.z - this.b;

        float delta = b * b - 4.f * a * c;
        if (delta <= 0.0f) {
            return null;
        }
        float sqrtDelta = (float) Math.sqrt(delta);

        float tMin = (-b - sqrtDelta) / (2.0f * a);
        float tMax = (-b + sqrtDelta) / (2.0f * a);
        float firstHitT;

        if (tMin < invRay.tMax && tMin > invRay.tMin && Math.abs(invRay.at(tMin).z) < 1.0f) {
            firstHitT = tMin;
        } else if (tMax < invRay.tMax && tMax > invRay.tMin && Math.abs(invRay.at(tMax).z) < 1.0f) {
            firstHitT = tMax;
        } else {
            return null;
        }

        Point hitPoint = invRay.at(firstHitT);

        float u = (float) ((Math.atan2(hitPoint.y, hitPoint.x) / (2.0 * Math.PI)) + 0.5);
        float v = (hitPoint.z + 1.0f) * 0.5f;

        var normal = new Normal(
                2.0f * this.a * hitPoint.x,
                2.0f * this.a * hitPoint.y,
                -2.0f * this.c * hitPoint.z
        );
        normal.normalize();

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
*/
    @Override
    public HitRecord rayIntersection(Ray ray) {
        Ray invRay = ray.transform(this.transformation.inverse());

        float a = invRay.dir.x * invRay.dir.x + invRay.dir.y * invRay.dir.y - invRay.dir.z * invRay.dir.z;
        float b = invRay.origin.x * invRay.dir.x + invRay.origin.y * invRay.dir.y - invRay.origin.z * invRay.dir.z;
        float c = invRay.origin.x * invRay.origin.x + invRay.origin.y * invRay.origin.y - invRay.origin.z * invRay.origin.z - 1.0F;
        float det4 = b * b - a * c;

        // Intersections
        float t1 = (-b - (float) Math.sqrt(det4)) / a;
        float t2 = (-b + (float) Math.sqrt(det4)) / a;

        float firstHitT;
        if (t1 >= ray.tMin && t1 <= ray.tMax && invRay.at(t1).z >= this.minZ && invRay.at(t1).z <= this.maxZ) {
            firstHitT = t1;
        } else if (t2 >= ray.tMin && t2 <= ray.tMax && invRay.at(t2).z >= this.minZ && invRay.at(t2).z <= this.maxZ) {
            firstHitT = t2;
        } else {
            return null;
        }

        Point hit = invRay.at(firstHitT);
        return new HitRecord(
                (Point) this.transformation.times(hit),
                (Normal) this.transformation.times(getNormal(hit, invRay.dir)),
                toSurPoint(hit),
                firstHitT,
                ray,
                this
        );
    }

    private Vec2d toSurPoint(Point hitPoint) {
        float u = (float) ((Math.atan2(hitPoint.y, hitPoint.x) + Math.PI) / (2.0 * Math.PI));
        float v = (hitPoint.z - minZ) / (maxZ - minZ);
        return new Vec2d(u, v);
    }



    private Normal getNormal(Point p, Vec rayDir) {
        Normal n = new Normal(p.x, p.y, -p.z);
        return (n.toVec().dot(rayDir) < 0.0F) ? n : (Normal) n.neg();
    }

    @Override
    public boolean isInternal(Point point) {
        Point realP = (Point) this.transformation.inverse().times(point);
        return realP.x * realP.x + realP.y * realP.y - realP.z * realP.z <= 1.0F && realP.z >= minZ && realP.z <= maxZ;
    }


    @Override
    public List<HitRecord> rayIntersectionList(Ray ray) {
        Ray invRay = ray.transform(this.transformation.inverse());

        float a = invRay.dir.x * invRay.dir.x + invRay.dir.y * invRay.dir.y - invRay.dir.z * invRay.dir.z;
        float b = invRay.origin.x * invRay.dir.x + invRay.origin.y * invRay.dir.y - invRay.origin.z * invRay.dir.z;
        float c = invRay.origin.x * invRay.origin.x + invRay.origin.y * invRay.origin.y - invRay.origin.z * invRay.origin.z - 1.0F;
        float det4 = b * b - a * c;

        // Intersections
        float t1 = (-b - (float) Math.sqrt(det4)) / a;
        float t2 = (-b + (float) Math.sqrt(det4)) / a;

        List<HitRecord> hits = new ArrayList<>();
        if (t1 >= ray.tMin && t1 <= ray.tMax && invRay.at(t1).z >= minZ && invRay.at(t1).z <= maxZ) {
            Point hit = invRay.at(t1);
            hits.add(new HitRecord(
                    (Point) this.transformation.times(hit),
                    (Normal) this.transformation.times(getNormal(hit, invRay.dir)),
                    toSurPoint(hit),
                    t1,
                    ray,
                    this
            ));
        }
        if (t2 >= ray.tMin && t2 <= ray.tMax && invRay.at(t2).z >= minZ && invRay.at(t2).z <= maxZ) {
            Point hit = invRay.at(t2);
            hits.add(new HitRecord(
                    (Point) this.transformation.times(hit),
                    (Normal) this.transformation.times(getNormal(hit, invRay.dir)),
                    toSurPoint(hit),
                    t2,
                    ray,
                    this
            ));
        }

        if (hits.isEmpty()) {
            return null;
        } else {
            Collections.sort(hits, Comparator.comparingDouble(h -> h.t));
            return hits;
        }
    }




}
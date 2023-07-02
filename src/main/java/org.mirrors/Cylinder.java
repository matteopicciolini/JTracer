package org.mirrors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.Math.sqrt;
import static org.mirrors.Global.VecZ;

public class Cylinder extends Shape {
    public Cylinder() {
        super();
    }
    public Cylinder(Transformation transformation) {
        super(transformation);
    }
    public Cylinder(Material material) {
        super(material);
    }
    public Cylinder(Transformation transformation, Material material) {
        super(transformation, material);
    }



    @Override
    public List<HitRecord> rayIntersectionList(Ray ray) {
        Ray invRay = ray.transform(this.transformation.inverse());

        float a = invRay.dir.x * invRay.dir.x + invRay.dir.y * invRay.dir.y;
        float b = 2.f * (invRay.dir.x * invRay.origin.x + invRay.dir.y * invRay.origin.y);
        float c = invRay.origin.x * invRay.origin.x + invRay.origin.y * invRay.origin.y - 1.f;

        float delta = b * b - 4.f * a * c;
        if (delta <= 0.0f) return null;
        float sqrtDelta = (float) sqrt(delta);
        float tMin = (-b - sqrtDelta) / (2.0f * a);
        float tMax = (-b + sqrtDelta) / (2.0f * a);

        List<HitRecord> intersections = new ArrayList<>();
        Point hitPoint1 = invRay.at(tMin);

        float phi1 = (float) Math.atan2(hitPoint1.y, hitPoint1.x);
        if (phi1 < 0) phi1 += 2.0f * (float) Math.PI;
        if (tMin < invRay.tMax && tMin > invRay.tMin && (hitPoint1.z > 0.0f && hitPoint1.z < 1.0f)) {
            float u = phi1 / (2.0f * (float) Math.PI);
            float v = hitPoint1.z;
            Normal normal = new Normal(hitPoint1.x, hitPoint1.y, 0.0f);
            if (normal.toVec().dot(invRay.dir) > 0.0f) normal = (Normal) normal.neg();
            intersections.add(new HitRecord(
                    (Point) this.transformation.times(hitPoint1),
                    (Normal) this.transformation.times(normal),
                    new Vec2d(u, v),
                    tMin,
                    ray,
                    this));
        }
        Point hitPoint2 = invRay.at(tMax);
        float phi2 = (float) Math.atan2(hitPoint2.y, hitPoint2.x);
        if (phi2 < 0) phi2 += 2.0f * (float) Math.PI;
        if (tMax < invRay.tMax && tMax > invRay.tMin && (hitPoint2.z > 0.0f && hitPoint2.z < 1.0f)) {
            float u = phi2 / (2.0f * (float) Math.PI);
            float v = hitPoint2.z;
            Normal normal = new Normal(hitPoint2.x, hitPoint2.y, 0.0f);
            if (normal.toVec().dot(invRay.dir) > 0.0f) normal = (Normal) normal.neg();
            intersections.add(new HitRecord(
                    (Point) this.transformation.times(hitPoint2),
                    (Normal) this.transformation.times(normal),
                    new Vec2d(u, v),
                    tMax,
                    ray,
                    this));
        }

        return intersections.isEmpty() ? null : intersections;
    }

    @Override
    public boolean isInternal(Point point) {
        point = (Point) this.transformation.inverse().times(point);
        float dist = point.x * point.x + point.y * point.y;
        return dist < 1.0f && (point.z > 0.0f && point.z < 1.0f);
    }





    @Override
    public HitRecord rayIntersection(Ray ray) {
        Ray invRay = ray.transform(this.transformation.inverse());
        float a = invRay.dir.x * invRay.dir.x + invRay.dir.y * invRay.dir.y;
        float b = 2.f * (invRay.dir.x * invRay.origin.x + invRay.dir.y * invRay.origin.y);
        float c = invRay.origin.x * invRay.origin.x + invRay.origin.y * invRay.origin.y - 1.f;

        float delta = b * b - 4.f * a * c;
        if (delta <= 0.0f) return null;
        float sqrtDelta = (float) sqrt(delta);

        // solutions for an infinitely long cylinder
        float tMin = (-b - sqrtDelta) / (2.0f * a);
        float tMax = (-b + sqrtDelta) / (2.0f * a);
        float firstHitT;

        if (tMin < invRay.tMax&& tMin > invRay.tMin && invRay.at(tMin).z > 0.0f && invRay.at(tMin).z < 1.0f) {
            firstHitT = tMin;
        } else if (tMax < invRay.tMax && tMax > invRay.tMin && invRay.at(tMax).z > 0.0f && invRay.at(tMax).z < 1.0f) {
            firstHitT = tMax;
        } else {
            return null;
        }

        Point hitPoint = invRay.at(firstHitT);

        float phi = (float) Math.atan2(hitPoint.y, hitPoint.x);
        if (phi < 0) phi += 2.0f * (float) Math.PI;

        float u = phi / (2.0f * (float) Math.PI);
        float v = hitPoint.z;

        var normal = new Normal(hitPoint.x, hitPoint.y, 0.0f);
        if (normal.toVec().dot(invRay.dir) > 0.0f) normal = (Normal) normal.neg();
        return new HitRecord(
                (Point) this.transformation.times(hitPoint),
                (Normal) this.transformation.times(normal),
                new Vec2d(u ,v),
                firstHitT,
                ray,
                this
        );
    }


    /*

    @Override
    public List<HitRecord> rayIntersectionList(Ray ray) {
        Ray invRay = ray.transform(this.transformation.inverse());
        List<HitRecord> hits = new ArrayList<>();

        float a = invRay.dir.x * invRay.dir.x + invRay.dir.y * invRay.dir.y;
        float b = 2.f * (invRay.dir.x * invRay.origin.x + invRay.dir.y * invRay.origin.y);
        float c = invRay.origin.x * invRay.origin.x + invRay.origin.y * invRay.origin.y - 1.f;

        float delta = b * b - 4.f * a * c;
        if (delta < 0.0f) return null;
        float sqrtDelta = (float) sqrt(delta);

        // solutions for an infinitely long cylinder
        float tMin = (-b - sqrtDelta) / (2.0f * a);
        float tMax = (-b + sqrtDelta) / (2.0f * a);



        float xypos = invRay.origin.x * invRay.origin.x + invRay.origin.y * invRay.origin.y;
        // tempo per raggiungere la faccia superiore
        float tzUp = (-Math.signum(invRay.dir.z) * 0.5f - invRay.origin.z) / invRay.dir.z;
        // tempo per raggiungere la faccia inferiore
        float tzDown = (Math.signum(invRay.dir.z) * 0.5f - invRay.origin.z) / invRay.dir.z;

        // Se l'origine del raggio si trova all'interno del cilindro infinito con r = 1
        // Verifica le possibili intersezioni con la faccia superiore/inferiore

        if (xypos < 1.0f) {
            if (tzUp >= ray.tMin && tzUp <= tMax) {
                Point hit = invRay.at(tzUp);
                hits.add(new HitRecord(
                        (Point) this.transformation.times(hit),
                        (Normal) this.transformation.times((((Vec) VecZ.neg()).toNormal()).dot(Math.signum(invRay.dir.z))),
                        toSurfacePoint(hit),
                        tzUp,
                        ray,
                        this
                ));
            }
            if (tzDown >= ray.tMin && tzDown <= tMax) {
                Point hit = invRay.at(tzDown);
                hits.add(new HitRecord(
                        (Point) this.transformation.times(hit),
                        (Normal) this.transformation.times((((Vec) VecZ.neg()).toNormal()).dot(Math.signum(invRay.dir.z))),
                        toSurfacePoint(hit),
                        tzDown,
                        ray,
                        this
                ));
            }
        }
        else if(tMin >= ray.tMin && tMin <= ray.tMax) {
            Point hit1 = invRay.at(tMin);
            if (hit1.z >= -0.5f && hit1.z <= 0.5f) {
                hits.add(new HitRecord(
                        (Point) this.transformation.times(hit1),
                        (Normal) this.transformation.times(this.getNormal(hit1, invRay.dir)),
                        toSurfacePoint(hit1),
                        tMin,
                        ray,
                        this
                ));
            } else if (hit1.z > 0.5f && invRay.dir.z < 0.0f) {
                float tmin = tMax - tMin;
                float dz = hit1.z - 0.5f;
                float tz = dz / Math.abs(invRay.dir.z);
                if (tz < tmin) {
                    tMin += tz;
                    Point hit = invRay.at(tMin);
                    hits.add(new HitRecord(
                            (Point) this.transformation.times(hit),
                            (Normal) this.transformation.times(VecZ.toNormal()),
                            toSurfacePoint(hit),
                            tMin,
                            ray,
                            this
                    ));
                } else {
                    return null;
                }
            }
            else if (hit1.z < -0.5f && invRay.dir.z > 0.0f) {
                float tmin = tMax - tMin;
                float dz = -0.5f - hit1.z;
                float tz = dz / invRay.dir.z;
                if (tz < tmin) {
                    tMin += tz;
                    Point hit = invRay.at(tMin);
                    hits.add(new HitRecord(
                            (Point) this.transformation.times(hit),
                            (Normal) this.transformation.times(((Vec) VecZ.neg()).toNormal()),
                            toSurfacePoint(hit),
                            tMin,
                            ray,
                            this
                    ));
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
        // inside the cylinder already checked for bases intersection
        // so we add eventually an intersection with the lateral surface
        if (xypos < 1.0f) {
            Point hit2 = invRay.at(tMax);
            if (tMax >= ray.tMin && tMax <= ray.tMax && hit2.z >= -0.5f && hit2.z <= 0.5f) {
                hits.add(new HitRecord(
                        (Point) this.transformation.times(hit2),
                        (Normal) this.transformation.times(getNormal(hit2, invRay.dir)),
                        toSurfacePoint(hit2),
                        tMax,
                        ray,
                        this
                ));
            }
        }
        // now check II intersection when origin outside the infinite cylinder
        else {
            if (tMax >= ray.tMin && tMax <= ray.tMax) {
                // Check if intersection point z component is in the cylinder
                Point hit2 = invRay.at(tMax);
                if (hit2.z >= -0.5f && hit2.z <= 0.5f) {
                    hits.add(new HitRecord(
                            (Point) this.transformation.times(hit2),
                            (Normal) this.transformation.times(getNormal(hit2, invRay.dir)),
                            toSurfacePoint(hit2),
                            tMax,
                            ray,
                            this
                    ));
                } else if (hit2.z > 0.5f) {
                    // the intersection will not be in hit2, but in the point
                    // where the ray meets the base
                    float dz = 0.5f - invRay.origin.z;
                    float tz = dz / invRay.dir.z;
                    Point hit = invRay.at(tz);
                    hits.add(new HitRecord(
                            (Point) this.transformation.times(hit),
                            (Normal) this.transformation.times(VecZ.toNormal().neg()),
                            toSurfacePoint(hit),
                            tz,
                            ray,
                            this
                    ));
                } else if (hit2.z < -0.5f) {
                    // the intersection will not be in hit2, but in the point
                    // where the ray meets the base
                    float dz = -0.5f - invRay.origin.z;
                    float tz = dz / invRay.dir.z;
                    Point hit = invRay.at(tz);
                    hits.add(new HitRecord(
                            (Point) this.transformation.times(hit),
                            (Normal) this.transformation.times((VecZ.toNormal())),
                            toSurfacePoint(hit),
                            tz,
                            ray,
                            this
                    ));
                } else {
                    return null;
                }
            }
        }


        if (hits.isEmpty()) {
            return null;
        } else {
            Collections.sort(hits, Comparator.comparingDouble(h -> h.t));
            return hits;
        }
    }

    @Override
    public boolean isInternal(Point point) {
        Point canP = (Point) this.transformation.inverse().times(point);
        return canP.x * canP.x + canP.y * canP.y <= 1.0f && canP.z >= -0.5f && canP.z <= 0.5f;
    }

    private Vec2d toSurfacePoint(Point hit) {
        if (this.isClose(hit.z, 0.5f)) {
            return new Vec2d(
                    0.5f + (hit.x + 1.0f) / 4.0f,
                    (hit.y + 1.0f) / 4.0f
            );
        } else if (this.isClose(hit.z, -0.5f)) {
            return new Vec2d(
                    (hit.x + 1.0f) / 4.0f,
                    (hit.y + 1.0f) / 4.0f
            );
        } else {
            return new Vec2d(
                    (float) (((Math.atan2(hit.y, hit.x) + (2.0 * Math.PI)) % (2.0 * Math.PI)) / (2.0f * Math.PI)),
                    1.0f - (hit.z + 0.5f) / 2.0f
            );
        }
    }

    private boolean isClose(float a, float b) {
        return Math.abs(a - b) <= 1e-6f;
    }

    private Normal getNormal(Point p, Vec rayDir) {
        Normal n = new Normal(p.x, p.y, 0.0f);
        return (p.x * rayDir.x + p.y * rayDir.y < 0.0f) ? n : (Normal) n.neg();
    }*/

}

package org.mirrors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a hyperboloid shape in 3D space.
 */
public class Hyperboloid extends Shape {
    /*
    private float a; // Parametro che influenza la dimensione del raggio dell'iperboloide lungo l'asse x
    private float b; // Parametro che influenza la dimensione del raggio dell'iperboloide lungo l'asse y
    private float c; // Parametro che influenza la dimensione del raggio dell'iperboloide lungo l'asse z
    */
    private float minZ = -2.5f;
    private float maxZ = 2.5f;

    /**
     * Constructs a hyperboloid with the specified transformation and material.
     *
     * @param transformation the transformation applied to the shape
     * @param material       the material of the shape
     */
    public Hyperboloid(Transformation transformation, Material material) {
        super(transformation, material);
        this.maxZ = 0.5f;
        this.minZ = -0.5f;
    }

    /**
     * Constructs a hyperboloid with the specified transformation, material, and z-coordinate limits.
     *
     * @param transformation the transformation applied to the shape
     * @param material       the material of the shape
     * @param minZ           the minimum z-coordinate value
     * @param maxZ           the maximum z-coordinate value
     */
    public Hyperboloid(Transformation transformation, Material material, float minZ, float maxZ) {
        super(transformation, material);
        this.maxZ = maxZ;
        this.minZ = minZ;
    }

    /**
     * {@inheritDoc}
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

    /**
     * Converts the hit point to surface coordinates (u, v).
     *
     * @param hitPoint the hit point in object space
     * @return the surface coordinates (u, v)
     */
    private Vec2d toSurPoint(Point hitPoint) {
        float u = (float) ((Math.atan2(hitPoint.y, hitPoint.x) + Math.PI) / (2.0 * Math.PI));
        float v = (hitPoint.z - minZ) / (maxZ - minZ);
        return new Vec2d(u, v);
    }


    /**
     * Calculates the surface normal at the given point.
     *
     * @param p      the point in object space
     * @param rayDir the direction of the ray
     * @return the surface normal at the point
     */
    private Normal getNormal(Point p, Vec rayDir) {
        Normal n = new Normal(p.x, p.y, -p.z);
        return (n.toVec().dot(rayDir) < 0.0F) ? n : (Normal) n.neg();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInternal(Point point) {
        Point realP = (Point) this.transformation.inverse().times(point);
        return realP.x * realP.x + realP.y * realP.y - realP.z * realP.z <= 1.0F && realP.z >= minZ && realP.z <= maxZ;
    }

    /**
     * {@inheritDoc}
     */
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
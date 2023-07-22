package org.mirrors;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.sqrt;

/**
 * Represents a cylinder shape in the scene.
 */
public class Cylinder extends Shape {

    /**
     * Constructs a cylinder with no transformation or material.
     */
    public Cylinder() {
        super();
    }

    /**
     * Constructs a cylinder with the specified transformation and no material.
     *
     * @param transformation the transformation for the cylinder
     */
    public Cylinder(Transformation transformation) {
        super(transformation);
    }

    /**
     * Constructs a cylinder with the specified material and no transformation.
     *
     * @param material the material for the cylinder
     */
    public Cylinder(Material material) {
        super(material);
    }

    /**
     * Constructs a cylinder with the specified transformation and material.
     *
     * @param transformation the transformation for the cylinder
     * @param material       the material for the cylinder
     */
    public Cylinder(Transformation transformation, Material material) {
        super(transformation, material);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<HitRecord> rayIntersectionList(Ray ray) {
        Ray invRay = ray.transform(this.transformation.inverse());

        float a = invRay.dir.x * invRay.dir.x + invRay.dir.y * invRay.dir.y;
        float b = 2.f * (invRay.dir.x * invRay.origin.x + invRay.dir.y * invRay.origin.y);
        float c = invRay.origin.x * invRay.origin.x + invRay.origin.y * invRay.origin.y - 1.f;

        float delta = b * b - 4.f * a * c;
        if (delta < 0.0f) return null;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInternal(Point point) {
        point = (Point) this.transformation.inverse().times(point);
        float dist = point.x * point.x + point.y * point.y;
        return dist <= 1.0f && point.z >= 0.0f && point.z <= 1.0f;
    }

    /**
     * {@inheritDoc}
     */
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

        if (tMin < invRay.tMax && tMin > invRay.tMin && invRay.at(tMin).z > 0.0f && invRay.at(tMin).z < 1.0f) {
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
                new Vec2d(u, v),
                firstHitT,
                ray,
                this
        );
    }
}

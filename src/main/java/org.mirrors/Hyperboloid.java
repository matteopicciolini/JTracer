package org.mirrors;

import java.util.List;

public class Hyperboloid extends Shape {
    private float a; // Parametro che influenza la dimensione del raggio dell'iperboloide lungo l'asse x
    private float b; // Parametro che influenza la dimensione del raggio dell'iperboloide lungo l'asse y
    private float c; // Parametro che influenza la dimensione del raggio dell'iperboloide lungo l'asse z

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

}
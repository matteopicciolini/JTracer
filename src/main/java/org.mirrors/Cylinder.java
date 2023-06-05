package org.mirrors;

import static java.lang.Math.sqrt;

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
    public HitRecord rayIntersection(Ray ray) throws InvalidMatrixException {
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
}

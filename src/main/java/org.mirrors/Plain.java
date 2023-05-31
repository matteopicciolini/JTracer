package org.mirrors;

import static java.lang.Math.*;

public class Plain extends Shape{
    public Plain() {
        super();
    }

    public Plain(Transformation transformation, Material material) {
        super(transformation, material);
    }

    @Override
    public HitRecord rayIntersection(Ray ray) throws InvalidMatrixException {
        Ray invRay = ray.transform(this.transformation.inverse());
        if (Math.abs(invRay.dir.z) < 1e-5) return null;

        float t = -invRay.origin.z / invRay.dir.z;
        if ((t <= invRay.tMin) || (t >= invRay.tMax)) return null;
        Point hitPoint = invRay.at(t);

        return new HitRecord((Point) this.transformation.times(hitPoint),
                (Normal) this.transformation.times(new Normal(0.0f, 0.0f, invRay.dir.z < 0.0f ? 1.f : -1.0f)),
                new Vec2d((float) (hitPoint.x - floor(hitPoint.x)), (float) (hitPoint.y - floor(hitPoint.y))),
                t,
                ray,
                this);
    }
}




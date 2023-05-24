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
        Ray inv_ray = ray.transform(this.transformation.inverse());
        if (Math.abs(inv_ray.dir.z) < 1e-5)
        return null;

        float t = -inv_ray.origin.z / inv_ray.dir.z;

        if ((t <= inv_ray.tMin) | (t >= inv_ray.tMax))
        return null;

        Point hit_point = inv_ray.at(t);


        float z;
        if (inv_ray.dir.z < 0.0f)
            z= 1.0f;
        else
            z=-1.0f;
        Point worldpoint= (Point) this.transformation.times(hit_point);
        Normal normal = (Normal) this.transformation.times(new Normal(0.0f, 0.0f, z));
        Vec2d Surface_point= new Vec2d((float) (hit_point.x - floor(hit_point.x)), (float) (hit_point.y - floor(hit_point.y)));
        return new HitRecord(worldpoint, normal, Surface_point, t, ray, this);
    }
}




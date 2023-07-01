package org.mirrors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class CSGUnion extends CSG{

    public CSGUnion(Transformation transformation, Material material, Shape shape1, Shape shape2) {
        super(transformation, material, shape1, shape2);
    }

    public CSGUnion(Shape shape1, Shape shape2) {
        super(shape1, shape2);
    }

    @Override
    public HitRecord rayIntersection(Ray ray) {
        List<HitRecord> rayIntersectionList = this.rayIntersectionList(ray);
        return rayIntersectionList == null ? null : rayIntersectionList.get(0);
    }

    @Override
    public List<HitRecord> rayIntersectionList(Ray ray) {
        Ray invRay = ray.transform(this.transformation.inverse());
        List<HitRecord> intersections1 = this.shape1.rayIntersectionList(invRay);
        List<HitRecord> intersections2 = this.shape2.rayIntersectionList(invRay);

        List<HitRecord> intersections = new ArrayList<HitRecord>();
        if (intersections1 != null) intersections.addAll(intersections1);
        if (intersections2 != null) intersections.addAll(intersections2);

        if (!intersections.isEmpty()) {
            intersections.sort(Comparator.comparingDouble(hitRecord -> (double) hitRecord.t));
            return intersections;
        } else {
            return null;
        }
    }

    @Override
    public boolean isInternal(Point point) {
        point = (Point) this.transformation.inverse().times(point);
        return this.shape1.isInternal(point) || shape2.isInternal(point);
    }

}

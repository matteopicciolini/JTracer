package org.mirrors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a union operation between two shapes in Constructive Solid Geometry (CSG).
 */
public class CSGUnion extends CSG{

    /**
     * Constructs a CSG union operation with the specified transformation, shape1, and shape2.
     *
     * @param transformation the transformation matrix for the CSG union operation
     * @param shape1         the first shape in the union operation
     * @param shape2         the second shape in the union operation
     */
    public CSGUnion(Transformation transformation, Shape shape1, Shape shape2) {
        super(transformation, shape1, shape2);
    }

    /**
     * Constructs a CSG union operation with the specified shape1 and shape2.
     *
     * @param shape1 the first shape in the union operation
     * @param shape2 the second shape in the union operation
     */
    public CSGUnion(Shape shape1, Shape shape2) {
        super(shape1, shape2);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HitRecord rayIntersection(Ray ray) {
        List<HitRecord> rayIntersectionList = this.rayIntersectionList(ray);
        return rayIntersectionList == null ? null : rayIntersectionList.get(0);
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInternal(Point point) {
        point = (Point) this.transformation.inverse().times(point);
        return this.shape1.isInternal(point) || shape2.isInternal(point);
    }
}

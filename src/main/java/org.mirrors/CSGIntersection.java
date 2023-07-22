package org.mirrors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents an intersection operation between two shapes in Constructive Solid Geometry (CSG).
 */
public class CSGIntersection extends CSG {

    /**
     * Constructs a CSG intersection operation with the specified transformation, shape1, and shape2.
     *
     * @param transformation the transformation matrix for the CSG intersection operation
     * @param shape1         the first shape in the intersection operation
     * @param shape2         the second shape in the intersection operation
     */
    public CSGIntersection(Transformation transformation, Shape shape1, Shape shape2) {
        super(transformation, shape1, shape2);
    }

    /**
     * Constructs a CSG intersection operation with the specified shape1 and shape2.
     *
     * @param shape1 the first shape in the intersection operation
     * @param shape2 the second shape in the intersection operation
     */
    public CSGIntersection(Shape shape1, Shape shape2) {
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
        List<HitRecord> inter1 = this.shape1.rayIntersectionList(invRay);
        if (inter1 == null) return null;

        for (int i = inter1.size() - 1; i >= 0; i--) {
            if (!this.shape2.isInternal(inter1.get(i).worldPoint)) {
                inter1.remove(i);
            }
        }

        List<HitRecord> inter2 = this.shape2.rayIntersectionList(invRay);
        if (inter2 == null) return null;
        for (int i = inter2.size() - 1; i >= 0; i--) {
            if (!this.shape1.isInternal(inter2.get(i).worldPoint)) {
                inter2.remove(i);
            }
        }

        List<HitRecord> intersection = new ArrayList<>();
        intersection.addAll(inter1);
        intersection.addAll(inter2);
        return intersection.size() != 0 ? intersection.stream()
                .sorted(Comparator.comparingDouble(hitRecord -> (double) hitRecord.t))
                .collect(Collectors.toList()) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInternal(Point point) {
        point = (Point) this.transformation.inverse().times(point);
        return this.shape1.isInternal(point) && shape2.isInternal(point);
    }
}

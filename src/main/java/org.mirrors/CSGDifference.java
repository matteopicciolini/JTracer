package org.mirrors;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a difference operation between two shapes in Constructive Solid Geometry (CSG).
 */
public class CSGDifference extends CSG{

    /**
     * Constructs a CSG difference operation with the specified transformation, shape1, and shape2.
     *
     * @param transformation the transformation matrix for the CSG difference operation
     * @param shape1         the first shape in the CSG difference operation
     * @param shape2         the second shape in the CSG difference operation
     */
    public CSGDifference(Transformation transformation, Shape shape1, Shape shape2) {
        super(transformation, shape1, shape2);
    }

    /**
     * Constructs a CSG difference operation with the specified shape1 and shape2.
     *
     * @param shape1 the first shape in the CSG difference operation
     * @param shape2 the second shape in the CSG difference operation
     */
    public CSGDifference(Shape shape1, Shape shape2) {
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

        if(intersections1 == null) return null;
        int dim1 = intersections1.size();
        for (int i = dim1 - 1; i >= 0; i--) {
            if (shape2.isInternal(intersections1.get(i).worldPoint)) {
                intersections1.remove(i);
            }
        }

        List<HitRecord> intersections2 = shape2.rayIntersectionList(invRay);
        if (intersections2 != null) {
            int dim2 = intersections2.size();
            for (int i = dim2 - 1; i >= 0; i--) {
                if (!shape1.isInternal(intersections2.get(i).worldPoint)) {
                    intersections2.remove(i);
                }
            }
        }

        List<HitRecord> intersections = new ArrayList<>();
        intersections.addAll(intersections1);
        if (intersections2 != null) {
            intersections.addAll(intersections2);
        }

        return intersections.size() != 0 ? intersections.stream()
                .sorted(Comparator.comparingDouble(hitRecord -> (double) hitRecord.t))
                .collect(Collectors.toList()) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInternal(Point point) {
        point = (Point) this.transformation.inverse().times(point);
        return this.shape1.isInternal(point) && !shape2.isInternal(point);
    }
}

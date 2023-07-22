package org.mirrors;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a world that contains a list of shapes that can be intersected by a ray to find the closest hit.
 */
public class World {
    List<Shape> shapes = new ArrayList<>();

    /**
     * Adds a shape to the world.
     *
     * @param shape the shape to add to the world.
     */
    public void addShape(Shape shape) {
        this.shapes.add(shape);
    }

    /**
     * Finds the closest intersection point of a ray with any of the shapes in the world.
     *
     * @param ray the ray to find the intersection point with.
     * @return the hit record of the closest intersection point if there is any, null otherwise.
     * @throws InvalidMatrixException if the transformation matrix is invalid.
     */
    public HitRecord rayIntersection(Ray ray) throws InvalidMatrixException {
        HitRecord closest = null;
        for (Shape shape : shapes) {
            HitRecord intersection = shape.rayIntersection(ray);
            if (Objects.isNull(intersection)) {
                continue;
            }
            if (Objects.isNull(closest) || intersection.t < closest.t) {
                closest = intersection;
            }
        }
        return closest;
    }


}

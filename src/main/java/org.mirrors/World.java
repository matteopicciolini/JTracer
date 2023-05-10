package org.mirrors;

import java.util.ArrayList;
import java.util.List;

public class World {
    List<Shape> shapes = new ArrayList<>();

    public void addShape(Shape shape){
        this.shapes.add(shape);
    }

    public HitRecord rayIntersection(Ray ray) throws InvalidMatrixException {
        HitRecord closest = null;
        for (Shape shape : shapes) {
            HitRecord intersection = shape.rayIntersection(ray);
            if (intersection == null) {
                continue;
            }
            if (closest == null || intersection.t < closest.t) {
                closest = intersection;
            }
        }
        return closest;
    }


}

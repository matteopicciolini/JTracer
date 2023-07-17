package org.mirrors;
import java.util.List;

/**
 * The Shape class is an abstract base class for all shapes in the scene.
 * It contains a transformation matrix and an abstract method for ray-object intersection.
 */
public abstract class Shape {
    public Transformation transformation;
    public Material material;

    /**
     * Constructor for the Shape class.
     * Initializes the transformation and material to default values.
     */
    public Shape(){
        this.transformation = new Transformation();
        this.material = new Material();
    }

    /**
     * Constructor for the Shape class.
     * Initializes the material to the given value and transformation to default values.
     *
     * @param material  the material of the shape
     */
    public Shape(Material material){
        this.transformation = new Transformation();
        this.material = material;
    }

    /**
     * Constructor for the Shape class.
     * Initializes the transformation to the given value and material to default values.
     *
     * @param transformation    the transformation to apply to the shape
     */
    public Shape(Transformation transformation){
        this.transformation = transformation;
        this.material = new Material();
    }

    /**
     * Constructor for the Shape class.
     * Initializes the transformation and material to the given values.
     *
     * @param transformation    the transformation to apply to the shape
     * @param material          the material of the shape
     */
    public Shape(Transformation transformation, Material material){
        this.transformation = transformation;
        this.material = material;
    }

    /**
     * Calculates the intersection between a ray and the shape.
     *
     * @param ray   the ray to intersect with the shape
     * @return      the hit record containing information about the intersection,
     *              or null if there is no intersection
     */
    public abstract HitRecord rayIntersection(Ray ray);

    /**
     * Calculates a list of intersections between a ray and the shape.
     *
     * @param ray   the ray to intersect with the shape
     * @return      the list of hit records containing information about the intersections,
     *              or null if there are no intersections
     */
    public abstract List<HitRecord> rayIntersectionList(Ray ray);

    /**
     * Checks if a point is internal to the shape.
     *
     * @param point     the point to check
     * @return          true if the point is internal to the shape, false otherwise
     */
    public abstract boolean isInternal(Point point);
}
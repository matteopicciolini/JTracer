package org.mirrors;


/**
 * The Shape class is an abstract base class for all shapes in the scene.
 * It contains a transformation matrix and an abstract method for ray-object intersection.
 */
public abstract class Shape {
    public Transformation trans;
    public Material material;

    public Shape(){
        this.trans = new Transformation();
    }
    public Shape(Material material){
        this.trans = new Transformation();
        this.material = new Material();
    }

    /**
     * Initializes a new Shape object with the specified transformation matrix.
     * @param trans The transformation matrix for the shape.
     */
    public Shape(Transformation trans){
        this.trans = trans;
    }
    public abstract HitRecord rayIntersection(Ray ray) throws InvalidMatrixException;
}

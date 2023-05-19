package org.mirrors;


/**
 * The Shape class is an abstract base class for all shapes in the scene.
 * It contains a transformation matrix and an abstract method for ray-object intersection.
 */
public abstract class Shape {
    public Transformation transformation;
    public Material material;

    public Shape(){
        this.transformation = new Transformation();
        this.material = new Material();
    }
    public Shape(Transformation transformation, Material material){
        this.transformation = transformation;
        this.material = material;
    }
    public abstract HitRecord rayIntersection(Ray ray) throws InvalidMatrixException;
}

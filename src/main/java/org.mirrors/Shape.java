package org.mirrors;

public abstract class Shape {
    public Transformation trans;

    public Shape(){
        this.trans = new Transformation();
    }
    public Shape(Transformation trans){
        this.trans = trans;
    }
    public abstract HitRecord rayIntersection(Ray ray) throws InvalidMatrixException;
}

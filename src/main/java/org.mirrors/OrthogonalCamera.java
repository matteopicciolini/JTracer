package org.mirrors;

public class OrthogonalCamera extends Camera {
    public OrthogonalCamera(float aspectRatio){
        this.aspectRatio = aspectRatio;
        this.transformation = new Transformation();
    }

    public OrthogonalCamera(float aspectRatio, Transformation transformation){
        this.aspectRatio = aspectRatio;
        this.transformation = transformation;
    }
    public Ray fireRay(float u, float v){
        Point origin = new Point(-1.f, (1.f - 2.f * u) * this.aspectRatio, 2.f * v - 1.f);
        return new Ray(origin, Global.VecX).transform(this.transformation);
    }
}
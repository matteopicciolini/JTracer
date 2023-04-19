package org.mirrors;

public class PerspectioveCamera extends Camera {
    public float distance;

    public PerspectioveCamera(float distance, float aspectRatio){
        this.distance = distance;
        this.aspectRatio = aspectRatio;
        this.transformation = new Transformation();
    }

    public PerspectioveCamera(float distance, float aspectRatio, Transformation transformation){
        this.distance = distance;
        this.aspectRatio = aspectRatio;
        this.transformation = transformation;
    }
    public Ray fireRay(float u, float v){
        Point origin = new Point(-this.distance, 0.f, 0.f);
        Vec direction = new Vec(this.distance, (1.f - 2.f * u) * this.aspectRatio, 2.f * v - 1.f);
        return new Ray(origin, Global.VecX).transform(this.transformation);
    }
}

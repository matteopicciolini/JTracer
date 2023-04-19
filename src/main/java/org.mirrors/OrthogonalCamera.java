package org.mirrors;

public class OrthogonalCamera extends Camera {
    public void fireRay(float u, float v){
        Point origin = new Point(-1.f, (1.f - 2.f * u) * this.aspectRatio, 2.f * v - 1.f);
        return new Ray(origin, direction, 1.0f).transform(this.transformation);
    }
}

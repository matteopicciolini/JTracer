package org.mirrors;

public abstract class Camera{
    float d;
    float aspectRatio;
    public abstract void fireRay(float u, float v);
}

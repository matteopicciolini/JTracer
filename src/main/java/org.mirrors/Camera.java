package org.mirrors;

public abstract class Camera{
    float aspectRatio;
    Transformation transformation;

    public abstract Ray fireRay(float u, float v);
}
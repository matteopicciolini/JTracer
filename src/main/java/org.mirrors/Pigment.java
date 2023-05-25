package org.mirrors;

public abstract class Pigment {

    public abstract Color getColor(float u, float v);
    public Color getColor(Vec2d uv){
        return getColor(uv.u, uv.v);
    }
}

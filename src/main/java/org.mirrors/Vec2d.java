package org.mirrors;

public class Vec2d {
    public float u;
    public float v;

    public Vec2d(){
        this.u = 0.f;
        this.v = 0.f;
    }

    public Vec2d(float u, float v){
        this.u = u;
        this.v = v;
    }

    public boolean isClose(Vec2d other){
        float epsilon = 1e-5f;
        return (Math.abs(this.u - other.u) < epsilon) &&
                (Math.abs(this.v - other.v) < epsilon);
    }
}

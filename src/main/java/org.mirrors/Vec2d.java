package org.mirrors;
/**
 * A two-dimensional vector class that represents a point on a 2D surface.
 */
public class Vec2d {
    public float u;
    public float v;

    /**
     * Constructs a Vec2d object with default values (0,0).
     */
    public Vec2d(){
        this.u = 0.f;
        this.v = 0.f;
    }

    /**
     * Constructs a Vec2d object with the specified values for u and v.
     * @param u The u component of the vector.
     * @param v The v component of the vector.
     */
    public Vec2d(float u, float v){
        this.u = u;
        this.v = v;
    }

    /**
     * Checks if this vector is close to another vector, within a small epsilon value.
     * @param other The other vector to compare against.
     * @return true if the vectors are close, false otherwise.
     */
    public boolean isClose(Vec2d other){
        float epsilon = 1e-5f;
        return (Math.abs(this.u - other.u) < epsilon) &&
                (Math.abs(this.v - other.v) < epsilon);
    }
}

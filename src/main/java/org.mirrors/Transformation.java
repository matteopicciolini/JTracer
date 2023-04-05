package org.mirrors;

public class Transformation {
    public float[] matrix;
    public float[] invMatrix;

    public Transformation(){
        this.matrix = Global.IdentityMatrix;
        this.invMatrix = Global.IdentityMatrix;
    }
    public Transformation(float[] matrix, float[] invMatrix){
        this.matrix = matrix;
        this.invMatrix = invMatrix;
    }
    public Transformation inverse() {
        return new Transformation(invMatrix, matrix);
    }

    public Transformation translation(Vec vec) {
        float[] m = new float[]{
                1.0f, 0.0f, 0.0f, vec.x,
                0.0f, 1.0f, 0.0f, vec.y,
                0.0f, 0.0f, 1.0f, vec.z,
                0.0f, 0.0f, 0.0f, 1.0f
        };
        float[] inv = new float[]{
                1.0f, 0.0f, 0.0f, -vec.x,
                0.0f, 1.0f, 0.0f, -vec.y,
                0.0f, 0.0f, 1.0f, -vec.z,
                0.0f, 0.0f, 0.0f, 1.0f
        };
        return new Transformation(m, inv);
    }

    public Transformation scaling(Vec vec) {
        float[] m = new float[]{
                vec.x, 0.0f, 0.0f, 0.0f,
                0.0f, vec.y, 0.0f, 0.0f,
                0.0f, 0.0f, vec.z, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f};
        float [] inv = new float[]{
                1.f / vec.x, 0.0f, 0.0f, 0.0f,
                0.0f, 1.f / vec.y, 0.0f, 0.0f,
                0.0f, 0.0f, 1.f / vec.z, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f};
        return new Transformation(m, inv);
    }
}

package org.mirrors;

public class Transformation {
    public Matrix4x4 matrix;
    public Matrix4x4 invMatrix;

    public Transformation(){
        this.matrix = new Matrix4x4();
        this.invMatrix = new Matrix4x4();
    }
    public Transformation(Matrix4x4 matrix, Matrix4x4 invMatrix){
        this.matrix = matrix ;
        this.invMatrix = invMatrix;
    }

    public boolean isConsistent(){
        Matrix4x4 prod = this.matrix.cross(this.invMatrix);
        return prod.isClose(new Matrix4x4());
    }
    public Transformation inverse() {
        return new Transformation(invMatrix, matrix);
    }

    public static Transformation translation(Vec vec) throws InvalidMatrix {
        Matrix4x4 m = new Matrix4x4(new float[]{
                1.0f, 0.0f, 0.0f, vec.x,
                0.0f, 1.0f, 0.0f, vec.y,
                0.0f, 0.0f, 1.0f, vec.z,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        Matrix4x4 inv = new Matrix4x4(new float[]{
                1.0f, 0.0f, 0.0f, -vec.x,
                0.0f, 1.0f, 0.0f, -vec.y,
                0.0f, 0.0f, 1.0f, -vec.z,
                0.0f, 0.0f, 0.0f, 1.0f
        });
        return new Transformation(m, inv);
    }

    public static Transformation scaling(Vec vec) throws InvalidMatrix {
        Matrix4x4 m = new Matrix4x4(new float[]{
                vec.x, 0.0f, 0.0f, 0.0f,
                0.0f, vec.y, 0.0f, 0.0f,
                0.0f, 0.0f, vec.z, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        Matrix4x4 inv = new Matrix4x4(new float[]{
                1.f / vec.x, 0.0f, 0.0f, 0.0f,
                0.0f, 1.f / vec.y, 0.0f, 0.0f,
                0.0f, 0.0f, 1.f / vec.z, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        return new Transformation(m, inv);
    }

    public Geometry times(Geometry other){
        if (other instanceof Vec){
            return new Vec(
                    other.x * this.matrix.getMatrixElement(0,0) +
                            other.y * this.matrix.getMatrixElement(0,1) +
                            other.z * this.matrix.getMatrixElement(0,2),
                    other.x * this.matrix.getMatrixElement(1,0) +
                            other.y * this.matrix.getMatrixElement(1,1) +
                            other.z * this.matrix.getMatrixElement(1,2),
                    other.x * this.matrix.getMatrixElement(2,0) +
                            other.y * this.matrix.getMatrixElement(2,1) +
                            other.z * this.matrix.getMatrixElement(2,2)
            );
        }
        else if (other instanceof Point){
            Point point = new Point(
                    this.matrix.getMatrixElement(0,0) * other.x +
                            this.matrix.getMatrixElement(0,1) * other.y +
                            this.matrix.getMatrixElement(0,2) * other.z +
                            this.matrix.getMatrixElement(0,3),
                    this.matrix.getMatrixElement(1,0) * other.x +
                            this.matrix.getMatrixElement(1,1) * other.y +
                            this.matrix.getMatrixElement(1,2) * other.z +
                            this.matrix.getMatrixElement(1,3),
                    this.matrix.getMatrixElement(2,0) * other.x +
                            this.matrix.getMatrixElement(2,1) * other.y +
                            this.matrix.getMatrixElement(2,2) * other.z +
                            this.matrix.getMatrixElement(2,3)
            );
            float lambda = this.matrix.getMatrixElement(3,0) * other.x +
                    this.matrix.getMatrixElement(3,1) * other.y +
                    this.matrix.getMatrixElement(3,2) * other.z +
                    this.matrix.getMatrixElement(3,3);

            if (Math.abs(lambda - 1.0f) < 1e-5) {
                return point;
            } else {
                return point.dot((float) 1./lambda);
            }
        }
        else if(other instanceof Normal){
            return new Normal(
                    other.x * this.invMatrix.getMatrixElement(0,0) +
                            other.y * this.invMatrix.getMatrixElement(1,0) +
                            other.z * this.invMatrix.getMatrixElement(2,0),
                    other.x * this.invMatrix.getMatrixElement(0,1) +
                            other.y * this.invMatrix.getMatrixElement(1,1) +
                            other.z * this.invMatrix.getMatrixElement(2,1),
                    other.x * this.invMatrix.getMatrixElement(0,2) +
                            other.y * this.invMatrix.getMatrixElement(1,2) +
                            other.z * this.invMatrix.getMatrixElement(2,2));
        }
        else{
            throw new IllegalArgumentException(String.format("Invalid type %s multiplied to a Transformation object", other.getClass()));
        }
    }

    public Transformation times(Transformation other){
        Matrix4x4 matrix = this.matrix.cross(other.matrix);
        Matrix4x4 invMatrix = other.invMatrix.cross(this.invMatrix);
        return new Transformation(matrix, invMatrix);
    }

    public boolean isClose(Transformation other){
        return this.matrix.isClose(other.matrix) && this.invMatrix.isClose(other.invMatrix);
    }

    public static Transformation rotation_x(float angle) throws InvalidMatrix {

        float sin = (float) Math.sin(Math.toRadians(angle));
        float cos = (float) Math.cos(Math.toRadians(angle));
        Matrix4x4 m = new Matrix4x4(new float[]{
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, cos, -sin, 0.0f,
                0.0f, sin, cos, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        Matrix4x4 invm = new Matrix4x4(new float[]{
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, cos, sin, 0.0f,
                0.0f, -sin, cos, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        return new Transformation(m, invm);
    }
    public static Transformation rotation_y(float angle) throws InvalidMatrix {

        float sin = (float) Math.sin(Math.toRadians(angle));
        float cos = (float) Math.cos(Math.toRadians(angle));
        Matrix4x4 m = new Matrix4x4(new float[]{
                cos, 0.0f, sin, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                -sin, 0.0f, cos, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        Matrix4x4 invm = new Matrix4x4(new float[]{
                cos, 0.0f, -sin, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                sin, 0.0f, cos, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        return new Transformation(m, invm);
    }
    public static Transformation rotation_z(float angle) throws InvalidMatrix {

        float sin = (float) Math.sin(Math.toRadians(angle));
        float cos = (float) Math.cos(Math.toRadians(angle));
        Matrix4x4 m = new Matrix4x4(new float[]{
                cos, -sin, 0.0f, 0.0f,
                sin, cos, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        Matrix4x4 invm = new Matrix4x4(new float[]{
                cos, sin, 0.0f, 0.0f,
                -sin, cos, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        return new Transformation(m, invm);
    }
}

package org.mirrors;

/**
 * A representation of a 3D transformation in computer graphics or game development.
 * The transformation can be used to transform objects in 3D space by applying translation,
 * rotation, and scaling operations.
 */
public class Transformation {
    public Matrix4x4 matrix;
    public Matrix4x4 invMatrix;

    /**
     * Constructs a new identity transformation.
     */
    public Transformation(){
        this.matrix = new Matrix4x4();
        this.invMatrix = new Matrix4x4();
    }

    /**
     * Constructs a new transformation with the given matrices.
     *
     * @param matrix    the transformation matrix
     * @param invMatrix the inverse of the transformation matrix
     */
    public Transformation(Matrix4x4 matrix, Matrix4x4 invMatrix){
        this.matrix = matrix ;
        this.invMatrix = invMatrix;
    }

    /**
     * Checks whether the matrix and its inverse are consistent.
     *
     * @return true if the product of the matrix and its inverse is close to the identity matrix,
     * false otherwise
     */
    public boolean isConsistent(){
        Matrix4x4 prod = this.matrix.cross(this.invMatrix);
        return prod.isClose(new Matrix4x4());
    }

    /**
     * Checks whether the given transformation is close to this transformation.
     *
     * @param other the other transformation to compare to
     * @return true if the matrix and inverse matrix of the given transformation are close to this
     * transformation's matrix and inverse matrix, false otherwise
     */
    public boolean isClose(Transformation other){
        return this.matrix.isClose(other.matrix) && this.invMatrix.isClose(other.invMatrix);
    }

    /**
     * Returns a new transformation representing the inverse of this transformation.
     *
     * @return a new transformation representing the inverse of this transformation
     */
    public Transformation inverse() {
        return new Transformation(invMatrix, matrix);
    }

    /**
     * Creates a new transformation representing a translation by the given vector.
     *
     * @param vec the translation vector
     * @return a new transformation representing the translation by the given vector
     * @throws InvalidMatrixException if the matrix is not invertible
     */
    public static Transformation translation(Vec vec) throws InvalidMatrixException {
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

    /**
     * Creates a new transformation representing a scaling by the given vector.
     *
     * @param vec the scaling vector
     * @return a new transformation representing the scaling by the given vector
     * @throws InvalidMatrixException if the matrix is not invertible
     */
    public static Transformation scaling(Vec vec) throws InvalidMatrixException {
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
    /**
     * Transform the given geometry object by this transformation.
     * PLease remember this method returns the product B*A instead of A*B
     * @param other the geometry object to be transformed
     * @return a new geometry object representing the transformed object
     * @throws IllegalArgumentException if the given geometry object is not a Vec, Point, or Normal
     */
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

    /**
     * Multiply this transformation with another transformation.
     *
     * @param other the transformation to be multiplied with this transformation
     * @return a new transformation representing the product of the two transformations
     */
    public Transformation times(Transformation other){
        Matrix4x4 matrix = this.matrix.cross(other.matrix);
        Matrix4x4 invMatrix = other.invMatrix.cross(this.invMatrix);
        return new Transformation(matrix, invMatrix);
    }

    public Ray times(Ray ray){
        return new Ray(
                (Point) this.times(ray.origin),
                (Vec) this.times(ray.dir),
                ray.tMin,
                ray.tMax,
                ray.depth
        );
    }
    /**
     * Returns a new Transformation object representing a rotation around the x-axis by the given angle.
     *
     * @param angle the angle of rotation in degrees
     * @return a new Transformation object representing the rotation
     * @throws InvalidMatrixException if there is an error creating the transformation matrix
     */
    public static Transformation rotationX(float angle) throws InvalidMatrixException {
        float sin = (float) Math.sin(Math.toRadians(angle));
        float cos = (float) Math.cos(Math.toRadians(angle));

        Matrix4x4 m = new Matrix4x4(new float[]{
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, cos, -sin, 0.0f,
                0.0f, sin, cos, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        Matrix4x4 invM = new Matrix4x4(new float[]{
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, cos, sin, 0.0f,
                0.0f, -sin, cos, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        return new Transformation(m, invM);
    }

    /**
     * Returns a new Transformation object representing a rotation around the y-axis by the given angle.
     *
     * @param angle the angle of rotation in degrees
     * @return a new Transformation object representing the rotation
     * @throws InvalidMatrixException if there is an error creating the transformation matrix
     */
    public static Transformation rotationY(float angle) throws InvalidMatrixException {
        float sin = (float) Math.sin(Math.toRadians(angle));
        float cos = (float) Math.cos(Math.toRadians(angle));

        Matrix4x4 m = new Matrix4x4(new float[]{
                cos, 0.0f, sin, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                -sin, 0.0f, cos, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        Matrix4x4 invM = new Matrix4x4(new float[]{
                cos, 0.0f, -sin, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                sin, 0.0f, cos, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        return new Transformation(m, invM);
    }

    /**
     * Returns a new Transformation object representing a rotation around the z-axis by the given angle.
     *
     * @param angle the angle of rotation in degrees
     * @return a new Transformation object representing the rotation
     * @throws InvalidMatrixException if there is an error creating the transformation matrix
     */
    public static Transformation rotationZ(float angle) throws InvalidMatrixException {
        float sin = (float) Math.sin(Math.toRadians(angle));
        float cos = (float) Math.cos(Math.toRadians(angle));

        Matrix4x4 m = new Matrix4x4(new float[]{
                cos, -sin, 0.0f, 0.0f,
                sin, cos, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        Matrix4x4 invM = new Matrix4x4(new float[]{
                cos, sin, 0.0f, 0.0f,
                -sin, cos, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f});
        return new Transformation(m, invM);
    }
}

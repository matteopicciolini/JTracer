package org.mirrors;
import static org.mirrors.Transformation.translation;

/**
 A class representing a ray in 3D space.
 */
public class Ray {
    Point origin = new Point();
    Vec dir = new Vec();
    float tMin = 1e-5f;
    float tMax = Float.POSITIVE_INFINITY;
    int depth = 0;

    /**
     Default constructor for Ray class.
     */
    public Ray() {}

    /**
     Constructor for Ray class.
     @param origin the origin point of the ray.
     @param dir the direction vector of the ray.
     @param depth the depth of the ray for tracing rays recursively.
     */
    public Ray(Point origin, Vec dir, int depth) {
        this.origin = origin;
        this.dir = dir;
        this.depth = depth;
    }

    public Ray(Point origin, Vec dir, float tMin, float tMax, int depth) {
        this.origin = origin;
        this.dir = dir;
        this.tMin = tMin;
        this.tMax = tMax;
        this.depth = depth;
    }

    /**
     Constructor for Ray class.
     @param origin the origin point of the ray.
     @param dir the direction vector of the ray.
     */
    public Ray(Point origin, Vec dir) {
        this.origin = origin;
        this.dir = dir;
    }

    /**
     Calculate the point on the ray at distance t from the origin.
     @param t the distance from the origin point of the ray.
     @return the point on the ray at distance t from the origin.
     @throws InvalidMatrixException if the matrix is not valid for the operation.
     */
    public Point at(float t) throws InvalidMatrixException {
        return (Point) (translation((Vec) this.dir.dot(t))).times(this.origin);
    }

    /**
     Check if two rays are close to each other.
     @param ray the ray to compare with this ray.
     @return true if the two rays have close origin points and direction vectors.
     */
    public boolean isClose(Ray ray) {
        boolean b1 = ray.origin.isClose(this.origin);
        boolean b2 = ray.dir.isClose(this.dir);
        return b1 && b2;
    }

    /**
     Transform the ray with a given transformation.
     @param transformation the transformation to apply to the ray.
     @return the transformed ray.
     */
    public Ray transform(Transformation transformation){
        return new Ray((Point) transformation.times(this.origin), (Vec) transformation.times(this.dir), this.depth);
    }
}

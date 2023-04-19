package org.mirrors;
import static org.mirrors.Transformation.translation;

public class Ray {
    Point origin = new Point();
    Vec dir = new Vec();
    float tMin = 1e-5f;
    float tMax = Float.POSITIVE_INFINITY;
    int depth = 0;

    public Ray() {}
    public Ray(Point origin, Vec dir, int depth) {
        this.origin = origin;
        this.dir = dir;
        this.depth = depth;
    }
    public Ray(Point origin, Vec dir) {
        this.origin = origin;
        this.dir = dir;
    }

    public Point at(float t) throws InvalidMatrixException {
        return (Point) (translation((Vec) this.dir.dot(t))).times(this.origin);
    }

    public boolean isClose(Ray ray1) {
        boolean b1 = ray1.origin.isClose(this.origin);
        boolean b2 = ray1.dir.isClose(this.dir);
        return b1 && b2;
    }
    public Ray transform(Transformation trans){
        return new Ray((Point) trans.times(this.origin), (Vec) trans.times(this.dir), this.depth);
    }
}

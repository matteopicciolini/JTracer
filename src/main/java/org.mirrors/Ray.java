package org.mirrors;

public class Ray {
    Point origin = new Point();
    Vec dir = new Vec();
    float tmin = 1e-5f;
    float tmax = Float.POSITIVE_INFINITY;
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
        Transformation trans = new Transformation();
        return (Point) (trans.translation((Vec) this.dir.dot(t))).times(this.origin);
    }


    public boolean isClose(Ray ray1) {
        boolean b1 = ray1.origin.isClose(this.origin);
        boolean b2 = ray1.dir.isClose(this.dir);
        if (b1 == true && b2 == true) {
            return true;
        } else {
            return false;
        }

    }
    public Ray transform(Transformation trans){
        Ray ray =new Ray((Point) trans.times(this.origin), (Vec) trans.times(this.dir), this.depth);
        return ray;
    }
}

package org.mirrors;

public class HitRecord {
    public Point worldPoint;
    public Normal normal;
    public Vec2d surfPoint;
    public float t;
    public Ray ray;

    public HitRecord() {
        this.worldPoint = new Point(0.f, 0.f, 0.f);
        this.normal = new Normal(0.f, 0.f, 0.f);
        this.surfPoint = new Vec2d(0.f, 0.f);
        this.t = 0.0f;
        this.ray = new Ray();
    }
    public HitRecord(Point worldPoint, Normal normal, Vec2d surfacePoint, float t, Ray ray) {
        this.worldPoint = worldPoint;
        this.normal = normal;
        this.surfPoint = surfacePoint;
        this.t = t;
        this.ray = ray;
    }

    @Override
    public String toString() {
        return "WorldPoint " + worldPoint.toString() + "\n" +
                normal.toString() + "\n" +
                "SurPoint: " + surfPoint.toString() + "\n" +
                "t " + t;
    }

    public boolean isClose(HitRecord other) {
        float epsilon = 1e-5f;
        return (this.worldPoint.isClose(other.worldPoint) &&
                this.normal.isClose(other.normal) &&
                this.surfPoint.isClose(other.surfPoint) &&
                (Math.abs(t - other.t) < epsilon) &&
                this.ray.isClose(other.ray));
    }
}

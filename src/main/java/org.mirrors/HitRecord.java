package org.mirrors;

/**
 * The HitRecord class represents a record of a ray-object intersection
 * and contains information about the intersection point, normal, surface point, ray, and time.
 */
public class HitRecord {
    public Point worldPoint;
    public Normal normal;
    public Vec2d surfPoint;
    public float t;
    public Ray ray;

    /**
     * Initializes a new HitRecord with default values.
     */
    public HitRecord() {
        this.worldPoint = new Point(0.f, 0.f, 0.f);
        this.normal = new Normal(0.f, 0.f, 0.f);
        this.surfPoint = new Vec2d(0.f, 0.f);
        this.t = 0.0f;
        this.ray = new Ray();
    }

    /**
     * Initializes a new HitRecord with the specified values.
     *
     * @param worldPoint The intersection point in world space.
     * @param normal The normal at the intersection point.
     * @param surfacePoint The surface point in object space.
     * @param t The time of the intersection.
     * @param ray The ray that intersected with the object.
     */
    public HitRecord(Point worldPoint, Normal normal, Vec2d surfacePoint, float t, Ray ray) {
        this.worldPoint = worldPoint;
        this.normal = normal;
        this.surfPoint = surfacePoint;
        this.t = t;
        this.ray = ray;
    }

    /**
     * Returns a string representation of the HitRecord object.
     * @return A string representation of the HitRecord object.
     */
    @Override
    public String toString() {
        return "WorldPoint " + worldPoint.toString() + "\n" +
                normal.toString() + "\n" +
                "SurPoint: " + surfPoint.toString() + "\n" +
                "t " + t;
    }

    /**
     * Determines whether this HitRecord is close to another HitRecord object.
     * @param other The other HitRecord object.
     * @return true if the two HitRecord objects are close; otherwise, false.
     */
    public boolean isClose(HitRecord other) {
        float epsilon = 1e-5f;
        return (this.worldPoint.isClose(other.worldPoint) &&
                this.normal.isClose(other.normal) &&
                this.surfPoint.isClose(other.surfPoint) &&
                (Math.abs(t - other.t) < epsilon) &&
                this.ray.isClose(other.ray));
    }
}

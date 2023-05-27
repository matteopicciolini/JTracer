package org.mirrors;

import static java.lang.Math.*;


/**
 * The Sphere class represents a sphere shape in the scene.
 * It inherits from the Shape class and overrides the rayIntersection method.
 */
public class Sphere extends Shape{

    /**
     * Initializes a new Sphere object with an identity transformation matrix.
     */
    public Sphere() {
        super();
    }
    public Sphere(Transformation transformation) {
        super(transformation);
    }
    public Sphere(Material material) {
        super(material);
    }
    public Sphere(Transformation transformation, Material material) {
        super(transformation, material);
    }

    /**
     * Computes the intersection of the given Ray object with the sphere and returns a HitRecord object.
     *
     * @param ray The Ray object to intersect with the sphere.
     * @return A HitRecord object representing the intersection point and other information.
     * @throws InvalidMatrixException If the transformation matrix is invalid.
     */
    @Override
    public HitRecord rayIntersection(Ray ray) throws InvalidMatrixException {
        Ray invRay = ray.transform(this.transformation.inverse());
        Vec originVec = invRay.origin.toVec();
        float a = invRay.dir.squaredNorm();
        float b = originVec.dot(invRay.dir);
        float c = originVec.squaredNorm() - 1.f;
        float delta4 = b * b - a * c;
        if (delta4 <= 0.f){
            return null;
        }

        float sqrtDelta4 = (float) sqrt(delta4);
        float tMin = (-b - sqrtDelta4) / a;
        float tMax = (-b + sqrtDelta4) / a;

        float firstHit;
        if ((tMin > invRay.tMin) && (tMin < invRay.tMax)){
                firstHit = tMin;
        }
        else if ((tMax > invRay.tMin) && (tMax < invRay.tMax)){
            firstHit = tMax;
        }
        else{
            return null;
        }

        Point hitPoint = invRay.at(firstHit);
        return new HitRecord((Point) this.transformation.times(hitPoint),
                (Normal) this.transformation.times(sphereNormal(hitPoint, invRay.dir)),
                spherePointToUV(hitPoint),
                firstHit,
                ray, this);
    }

    /**
     * Computes the normal of the sphere at the given point and with the given ray direction.
     * @param point The point on the sphere to compute the normal for.
     * @param rayDir The direction of the incoming ray.
     * @return A Normal object representing the normal of the sphere at the given point.
     */
    private Normal sphereNormal(Point point, Vec rayDir){
        return point.toVec().dot(rayDir) < 0.f ?
                new Normal(point.x, point.y, point.z) :
                new Normal(-point.x, -point.y, -point.z);
    }

    /**
     * Maps a point on the sphere to a (u,v) coordinate pair.
     * @param point The point on the sphere to map to (u,v) coordinates.
     * @return A Vec2d object representing the (u,v) coordinates of the given point on the sphere.
     */
    private Vec2d spherePointToUV(Point point){
        float u = (float) (atan2(point.y, point.x) / (2.f * PI));
        return new Vec2d(
                u >= 0.f ? u : u + 1.f,
                (float) (acos(point.z) / PI)
        );
    }
}

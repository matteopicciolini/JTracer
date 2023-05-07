package org.mirrors;

import static java.lang.Math.*;

public class Sphere extends Shape{

    public Sphere() {
        super();
    }
    public Sphere(Transformation trans) {
        super(trans);
    }

    @Override
    public HitRecord rayIntersection(Ray ray) throws InvalidMatrixException {
        Ray invRay = ray.transform(this.trans.inverse());
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
        return new HitRecord((Point) this.trans.times(hitPoint),
                (Normal) this.trans.times(sphereNormal(hitPoint, invRay.dir)),
                spherePointToUV(hitPoint),
                firstHit,
                ray);
    }

    private Normal sphereNormal(Point point, Vec rayDir){
        return point.toVec().dot(rayDir) < 0.f ?
                new Normal(point.x, point.y, point.z) :
                new Normal(-point.x, -point.y, -point.z);
    }

    private Vec2d spherePointToUV(Point point){
        float u = (float) (atan2(point.y, point.x) / (2.0 * PI));
        return new Vec2d(
                u >= 0.0 ? u : (float) (u + 1.0),
                (float) (acos(point.z) / PI)
        );
    }
}

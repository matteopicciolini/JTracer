package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.InvVecZ;
import static org.mirrors.Global.VecZ;

class SphereTest {

    @Test
    void rayIntersection() throws InvalidMatrixException {
        Sphere sphere = new Sphere();
        Ray ray1 = new Ray(new Point(0.f, 0.f, 2.f), InvVecZ);
        HitRecord intersection1 = sphere.rayIntersection(ray1);
        HitRecord trueIntersection = new HitRecord(new Point(0.f, 0.f, 1.f),
                new Normal(0.f, 0.f, 1.f),
                new Vec2d(0.f, 0.f),
                1.f, ray1);
        assertTrue(trueIntersection.isClose(intersection1));
    }
}
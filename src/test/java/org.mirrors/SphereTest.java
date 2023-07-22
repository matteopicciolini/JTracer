package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.*;

class SphereTest {

    @Test
    void rayIntersection() throws InvalidMatrixException {
        Sphere sphere = new Sphere();

        Ray ray1 = new Ray(new Point(0.f, 0.f, 2.f), InvVecZ);
        HitRecord intersection1 = sphere.rayIntersection(ray1);
        HitRecord trueIntersection1 = new HitRecord(new Point(0.f, 0.f, 1.f),
                new Normal(0.f, 0.f, 1.f),
                new Vec2d(0.f, 0.f),
                1.f, ray1, new Sphere());
        assertNotNull(intersection1);
        assertTrue(trueIntersection1.isClose(intersection1));

        Ray ray2 = new Ray(new Point(3.f, 0.f, 0.f), InvVecX);
        HitRecord intersection2 = sphere.rayIntersection(ray2);
        HitRecord trueIntersection2 = new HitRecord(new Point(1.f, 0.f, 0.f),
                new Normal(1.f, 0.f, 0.f),
                new Vec2d(0.f, 0.5f),
                2.f, ray2, new Sphere());
        assertNotNull(intersection2);
        assertTrue(trueIntersection2.isClose(intersection2));

        Ray ray3 = new Ray(new Point(0.f, 0.f, 0.f), VecX);
        HitRecord intersection3 = sphere.rayIntersection(ray3);
        HitRecord trueIntersection3 = new HitRecord(new Point(1.f, 0.f, 0.f),
                new Normal(-1.f, 0.f, 0.f),
                new Vec2d(0.f, 0.5f),
                1.f, ray3, new Sphere());
        assertNotNull(intersection3);
        assertTrue(trueIntersection3.isClose(intersection3));
    }

    @Test
    void sphereTransformation() throws InvalidMatrixException {
        Sphere sphere = new Sphere(Transformation.translation(new Vec(10.f, 0.f, 0.f)), new Material());

        Ray ray1 = new Ray(new Point(10.f, 0.f, 2.f), InvVecZ);
        HitRecord intersection1 = sphere.rayIntersection(ray1);
        HitRecord trueIntersection1 = new HitRecord(
                new Point(10.f, 0.f, 1.f),
                new Normal(0.f, 0.f, 1.f),
                new Vec2d(0.f, 0.f),
                1.f, ray1, new Sphere());
        assertNotNull(intersection1);
        assertTrue(trueIntersection1.isClose(intersection1));

        Ray ray2 = new Ray(new Point(13.f, 0.f, 0.f), InvVecX);
        HitRecord intersection2 = sphere.rayIntersection(ray2);
        HitRecord trueIntersection2 = new HitRecord(
                new Point(11.f, 0.f, 0.f),
                new Normal(1.f, 0.f, 0.f),
                new Vec2d(0.f, 0.5f),
                2.f, ray2, new Sphere());
        assertNotNull(intersection2);
        assertTrue(trueIntersection2.isClose(intersection2));

        assertNull(sphere.rayIntersection(new Ray(new Point(0.f, 0.f, 2.f), InvVecZ)));
        assertNull(sphere.rayIntersection(new Ray(new Point(-10.f, 0.f, 0.f), InvVecZ)));
    }
}
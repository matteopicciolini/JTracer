package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.*;

class PlainTest {
    @Test
    void TestHit() throws InvalidMatrixException {
        Plain plane = new Plain();

        Ray ray1 = new Ray(new Point(0f, 0f, 1f), InvVecZ);
        HitRecord intersection1 = plane.rayIntersection(ray1);
        assertNotNull(intersection1);
        assertTrue((intersection1.isClose(new HitRecord(
                new Point(0.0f, 0.0f, 0.0f),
                new Normal(0.0f, 0.0f, 1.0f),
                new Vec2d(0.0f, 0.0f),
                1.0f,
                ray1,
                plane))));


        Ray ray2 = new Ray(new Point(0f, 0f, 1f), VecZ);
        HitRecord intersection2 = plane.rayIntersection(ray2);
        assertNull(intersection2);

        Ray ray3 = new Ray(new Point(0f, 0f, 1f), VecX);
        HitRecord intersection3 = plane.rayIntersection(ray3);
        assertNull(intersection3);

        Ray ray4 = new Ray(new Point(0, 0, 1), VecY);
        HitRecord intersection4 = plane.rayIntersection(ray4);
        assertNull(intersection4);
    }


    @Test
    void testTransformation() throws InvalidMatrixException {
        Plain plane = new Plain(Transformation.rotationY(90f), new Material());
        Ray ray1 = new Ray(new Point(1, 0, 0), InvVecX);
        HitRecord intersection1 = plane.rayIntersection(ray1);
        assertNotNull(intersection1);
        assertTrue(intersection1.isClose(new HitRecord(
                new Point(0.0f, 0.0f, 0.0f),
                new Normal(1.0f, 0.0f, 0.0f),
                new Vec2d(0.0f, 0.0f),
                1.0f,
                ray1,
                plane)));

        Ray ray2 = new Ray(new Point(0, 0, 1), VecZ);
        HitRecord intersection2 = plane.rayIntersection(ray2);
        assertNull(intersection2);

        Ray ray3 = new Ray(new Point(0, 0, 1), VecX);
        HitRecord intersection3 = plane.rayIntersection(ray3);
        assertNull(intersection3);

        Ray ray4 = new Ray(new Point(0, 0, 1), VecY);
        HitRecord intersection4 = plane.rayIntersection(ray4);
        assertNull(intersection4);
    }

    @Test
    void testUVCoordinates() throws InvalidMatrixException {
        Plain plane = new Plain();

        Ray ray1 = new Ray(new Point(0, 0, 1), InvVecZ);
        HitRecord intersection1 = plane.rayIntersection(ray1);
        assertTrue(intersection1.surfPoint.isClose(new Vec2d(0.0f, 0.0f)));

        Ray ray2 = new Ray(new Point(0.25f, 0.75f, 1), InvVecZ);
        HitRecord intersection2 = plane.rayIntersection(ray2);
        assertTrue(intersection2.surfPoint.isClose(new Vec2d(0.25f, 0.75f)));
        ;

        Ray ray3 = new Ray(new Point(4.25f, 7.75f, 1), InvVecZ);
        HitRecord intersection3 = plane.rayIntersection(ray3);
        assertTrue(intersection3.surfPoint.isClose(new Vec2d(0.25f, 0.75f)));
    }
}

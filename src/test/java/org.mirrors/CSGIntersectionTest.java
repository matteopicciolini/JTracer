package org.mirrors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.VecY;

import org.junit.jupiter.api.Test;

public class CSGIntersectionTest {

    @Test
    public void isInternal() throws InvalidMatrixException {
        Sphere s1 = new Sphere(Transformation.translation((Vec) VecY.dot(-0.5f)));
        Sphere s2 = new Sphere(Transformation.translation((Vec) VecY.dot(0.5f)));
        CSGIntersection u = new CSGIntersection(s1, s2);
        assertFalse(u.isInternal(new Point(0.0f, -0.75f, 0.0f)));
        assertTrue(u.isInternal(new Point()));
    }

    @Test
    public void rayIntersection() throws InvalidMatrixException {
        Sphere s1 = new Sphere(Transformation.translation((Vec) VecY.dot(-0.5f)));
        Sphere s2 = new Sphere(Transformation.translation((Vec) VecY.dot(0.5f)));
        CSGIntersection u = new CSGIntersection(s1, s2);

        Ray ray1 = new Ray(new Point(0.0f, 2.0f, 0.0f), (Vec) VecY.neg());
        Point expectedHitPoint0 = new Point(0.0f, 0.5f, 0.0f);
        Point expectedHitPoint1 = new Point(0.0f, -0.5f, 0.0f);
        Normal expectedNormal = VecY.toNormal();
        Vec2d expectedUV0 = new Vec2d(0.25f, 0.5f);
        Vec2d expectedUV1 = new Vec2d(0.75f, 0.5f);
        float expectedT0 = 1.5f;
        float expectedT1 = 2.5f;
        HitRecord int0 = u.rayIntersectionList(ray1).get(0);
        HitRecord int1 = u.rayIntersectionList(ray1).get(1);

        assertTrue(int0.worldPoint.isClose(expectedHitPoint0));
        assertTrue(int0.normal.isClose(expectedNormal));
        assertEquals(int0.t, (expectedT0));
        assertTrue(int0.surfPoint.isClose(expectedUV0));

        assertTrue(int1.worldPoint.isClose(expectedHitPoint1));
        assertTrue(int1.normal.isClose(expectedNormal));
        assertEquals(int1.t, expectedT1);
        assertTrue(int1.surfPoint.isClose(expectedUV1));
    }
}
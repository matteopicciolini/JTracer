package org.mirrors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.VecY;

import org.junit.jupiter.api.Test;

public class HyperboloidTest {

    @Test
    public void isInternal() {
        Transformation transformation = new Transformation();
        Material material = new Material();
        Hyperboloid hyperboloid = new Hyperboloid(transformation, material);

        assertFalse(hyperboloid.isInternal(new Point(0.0f, -2f, 0.0f)));
        assertTrue(hyperboloid.isInternal(new Point()));
    }

    @Test
    public void rayIntersection() {
        Transformation transformation = new Transformation();
        Material material = new Material();
        Hyperboloid hyperboloid = new Hyperboloid(transformation, material);

        Ray ray = new Ray(new Point(0.0f, 2.0f, 0.0f), (Vec) VecY.neg());
        Point expectedHitPoint0 = new Point(0.0f, 1.f, 0.0f);
        Point expectedHitPoint1 = new Point(0.0f, -1.f, 0.0f);
        Normal expectedNormal = VecY.toNormal();
        Vec2d expectedUV0 = new Vec2d(0.75f, 0.5f);
        Vec2d expectedUV1 = new Vec2d(0.25f, 0.5f);
        float expectedT0 = 1.f;
        float expectedT1 = 3f;
        HitRecord int0 = hyperboloid.rayIntersectionList(ray).get(0);
        HitRecord int1 = hyperboloid.rayIntersectionList(ray).get(1);

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

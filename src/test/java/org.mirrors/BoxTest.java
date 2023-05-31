package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.*;

class BoxTest {
    @Test
    void rayIntersection() throws InvalidMatrixException {
        Box b = new Box();
        Ray ray1 = new Ray(new Point(0.0F, 0.0F, 2.0F), InvVecZ);
        Point expectedHitPoint1 = new Point(0.0F, 0.0F, 0.5F);
        Normal expectedNormal1 = VecZ.toNormal();
        Vec2d expectedUV1 = new Vec2d(0.25F + 0.5F / 4.0F, 0.5F + 0.5F / 4.0F);
        float expectedT1 = 1.5F;
        HitRecord int1 = b.rayIntersection(ray1);

        System.out.println(int1.surfPoint);
        assertTrue(int1.worldPoint.isClose(expectedHitPoint1));
        assertTrue(int1.normal.isClose(expectedNormal1));
        assertEquals(int1.t, expectedT1, 1e-3);
        assertTrue(int1.surfPoint.isClose(expectedUV1));


        Ray ray2 = new Ray(new Point(3.0F, 0.0F, 0.0F), InvVecX);
        Point expectedHitPoint2 = new Point(0.5F, 0.0F, 0.0F);
        Normal expectedNormal2 = VecX.toNormal();
        Vec2d expectedUV2 = new Vec2d(0.5F + 0.5F / 4.0F, 0.5F + 0.5F / 4.0F);
        float expectedT2 = 2.5F;
        HitRecord int2 = b.rayIntersection(ray2);

        assertTrue(int2.worldPoint.isClose(expectedHitPoint2));
        assertTrue(int2.normal.isClose(expectedNormal2));
        assertEquals(int2.t, expectedT2, 1e-3);
        assertTrue(int2.surfPoint.isClose(expectedUV2));

        Ray ray3 = new Ray(new Point(0.0F, 0.0F, 0.0F), VecX);
        Point expectedHitPoint3 = new Point(0.5F, 0.0F, 0.0F);
        Normal expectedNormal3 = InvVecX.toNormal();
        Vec2d expectedUV3 = new Vec2d(0.5F / 4.0F, 0.5F + 0.5F / 4.0F);
        float expectedT3 = 0.5F;
        HitRecord int3 = b.rayIntersection(ray3);

        assertTrue(int3.worldPoint.isClose(expectedHitPoint3));
        assertTrue(int3.normal.isClose(expectedNormal3));
        assertEquals(int3.t, expectedT3, 1e-3);
        assertTrue(int3.surfPoint.isClose(expectedUV3));

        Ray ray4 = new Ray(new Point(0.5F, 0.5F, 1.0F), InvVecZ);
        Point expectedHitPoint4 = new Point(0.5F, 0.5F, 0.5F);
        Normal expectedNormal4 = VecZ.toNormal();
        Vec2d expectedUV4 = new Vec2d(0.5F, 0.5F);
        float expectedT4 = 0.5F;
        HitRecord int4 = b.rayIntersection(ray4);

        assertTrue(int4.worldPoint.isClose(expectedHitPoint4));
        assertTrue(int4.normal.isClose(expectedNormal4));
        assertEquals(int4.t, expectedT4, 1e-3);
        assertTrue(int4.surfPoint.isClose(expectedUV4));
    }

}
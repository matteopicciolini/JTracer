package org.mirrors;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mirrors.Global.VecZ;
import static org.mirrors.Global.VecX;
import static org.mirrors.Global.VecY;
import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {
    @Test
    public void isPointInternal() throws InvalidMatrixException {
        Cylinder c = new Cylinder();
        assertTrue(c.isInternal(new Point()));

        Cylinder c1 = new Cylinder(Transformation.translation((Vec) VecY.dot(2.f)));
        assertTrue(c1.isInternal(new Point(0.0f, 1.5f, 0.24f)));
    }

    @Test
    public void rayIntersection() {
        Cylinder c = new Cylinder(new Transformation());
        Ray ray1 = new Ray(new Point(2.0f, 0.0f, 0.0f), (Vec) VecX.neg());
        Point expectedHitPoint1 = new Point(1.0f, 0.0f, 0.0f);
        Normal expectedNormal1 = VecX.toNormal();
        Vec2d expectedUV1 = new Vec2d(0.0f, 0.75f);
        float expectedT1 = 1.0f;
        HitRecord int1 = c.rayIntersection(ray1);

        assertTrue(int1.worldPoint.isClose(expectedHitPoint1));
        assertTrue(int1.normal.isClose(expectedNormal1));
        assertEquals(int1.t, expectedT1, 1e-4);
        assertTrue(int1.surfPoint.isClose(expectedUV1));



        Ray ray2 = new Ray(new Point(-3.0f, 0.0f, 0.0f), VecX);
        Point expectedHitPoint2 = new Point(-1.0f, 0.0f, 0.0f);
        Normal expectedNormal2 = ((Vec) VecX.neg()).toNormal();
        Vec2d expectedUV2 = new Vec2d(0.5f, 0.75f);
        float expectedT2 = 2.0f;
        HitRecord int2 = c.rayIntersection(ray2);

        assertTrue(int2.worldPoint.isClose(expectedHitPoint2));
        assertTrue(int2.normal.isClose(expectedNormal2));
        assertEquals(int2.t, expectedT2, 1e-4);
        assertTrue(int2.surfPoint.isClose(expectedUV2));



        Ray ray3 = new Ray(new Point(0.0f, 0.0f, 0.0f), VecY);
        Point expectedHitPoint3 = new Point(0.0f, 1.0f, 0.0f);
        Normal expectedNormal3 = ((Vec) VecY.neg()).toNormal();
        Vec2d expectedUV3 = new Vec2d(0.25f, 0.75f);
        float expectedT3 = 1.0f;
        HitRecord int3 = c.rayIntersection(ray3);

        System.out.println(int3);

        assertTrue(int3.worldPoint.isClose(expectedHitPoint3));
        assertTrue(int3.normal.isClose(expectedNormal3));
        assertEquals(int3.t, expectedT3, 1e-4);
        assertTrue(int3.surfPoint.isClose(expectedUV3));



        Ray ray4 = new Ray(new Point(-2.0f, 0.0f, 2.5f), (VecX.minus(VecZ)));
        Point expectedHitPoint4 = new Point(0.0f, 0.0f, 0.5f);
        Normal expectedNormal4 = VecZ.toNormal();
        Vec2d expectedUV4 = new Vec2d(0.75f, 0.25f);
        float expectedT4 = 2.0f;
        HitRecord int4 = c.rayIntersection(ray4);

        assertTrue(int4.worldPoint.isClose(expectedHitPoint4));
        assertTrue(int4.normal.isClose(expectedNormal4));
        assertEquals(int4.t, expectedT4, 1e-4);
        assertTrue(int4.surfPoint.isClose(expectedUV4));



        Ray ray5 = new Ray(new Point(-0.5f, 0.0f, 2.5f), (((Vec) VecX.dot(0.1f)).minus(VecZ)));
        Point expectedHitPoint5 = new Point(-0.3f, 0.0f, 0.5f);
        Normal expectedNormal5 = VecZ.toNormal();
        Vec2d expectedUV5 = new Vec2d(0.675f, 0.25f);
        float expectedT5 = 2.0f;
        HitRecord int5 = c.rayIntersection(ray5);

        assertTrue(int5.worldPoint.isClose(expectedHitPoint5));
        assertTrue(int5.normal.isClose(expectedNormal5));
        assertEquals(int5.t, expectedT5, 1e-4);
        assertTrue(int5.surfPoint.isClose(expectedUV5));
    }

    @Test
    public void rayIntersectionList() {
        Cylinder c = new Cylinder();

        Ray ray = new Ray(new Point(0.0f, 0.0f, 2.0f), (Vec) VecZ.neg());
        Point expectedHitPoint0 = new Point(0.0f, 0.0f, 0.5f);
        Point expectedHitPoint1 = new Point(0.0f, 0.0f, -0.5f);
        Normal expectedNormal = VecZ.toNormal();
        Vec2d expectedUV0 = new Vec2d(0.75f, 0.25f);
        Vec2d expectedUV1 = new Vec2d(0.25f, 0.25f);
        float expectedT0 = 1.5f;
        float expectedT1 = 2.5f;
        List<HitRecord> intersections = c.rayIntersectionList(ray);
        HitRecord int0 = intersections.get(0);
        HitRecord int1 = intersections.get(1);

        assertTrue(int0.worldPoint.isClose(expectedHitPoint0));
        assertTrue(int0.normal.isClose(expectedNormal));
        assertEquals(int0.t, expectedT0, 1e-4);
        assertTrue(int0.surfPoint.isClose(expectedUV0));

        assertTrue(int1.worldPoint.isClose(expectedHitPoint1));
        assertTrue(int1.normal.isClose(expectedNormal));
        assertEquals(int1.t, expectedT1, 1e-4);
        assertTrue(int1.surfPoint.isClose(expectedUV1));

        Ray ray2 = new Ray(new Point(3.0f, 0.0f, 0.0f), ((Vec) VecX.neg()).sum((Vec) VecZ.dot(0.1f)));

        Point expectedHitPoint02 = new Point(1.0f, 0.0f, 0.2f);
        Point expectedHitPoint12 = new Point(-1.0f, 0.0f, 0.4f);
        Normal expectedNormal2 = VecX.toNormal();
        Vec2d expectedUV02 = new Vec2d(0.0f, 0.65f);
        Vec2d expectedUV12 = new Vec2d(0.5f, 0.55f);
        float expectedT02 = 2.0f;
        float expectedT12 = 4.0f;
        intersections = c.rayIntersectionList(ray2);
        HitRecord int02 = intersections.get(0);
        HitRecord int12 = intersections.get(1);

        assertTrue(int02.worldPoint.isClose(expectedHitPoint02));
        assertTrue(int02.normal.isClose(expectedNormal2));
        assertEquals(int02.t, expectedT02, 1e-4);
        assertTrue(int02.surfPoint.isClose(expectedUV02));

        assertTrue(int12.worldPoint.isClose(expectedHitPoint12));
        assertTrue(int12.normal.isClose(expectedNormal2));
        assertEquals(int12.t, expectedT12, 1e-4);
        assertTrue(int12.surfPoint.isClose(expectedUV12));
    }
}
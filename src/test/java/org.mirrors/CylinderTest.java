package org.mirrors;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CylinderTest {

    @Test
    public void rayIntersectionTest() {
        Cylinder cylinder = new Cylinder();
        Ray ray1 = new Ray(new Point(2.0f, 0.0f, 0.5f), new Vec(-1.0f, 0.0f, 0.0f));
        HitRecord intersection1 = cylinder.rayIntersection(ray1);
        assertNotNull(intersection1);
        assertTrue(intersection1.worldPoint.isClose(new Point(1.0f, 0.0f, 0.5f)));
        assertTrue(intersection1.surfPoint.isClose(new Vec2d(0.0f, 0.5f)));
        assertTrue(intersection1.normal.isClose(new Normal(1.0f, 0.0f, 0.0f)));
        assertTrue(intersection1.ray.isClose(ray1));
        assertEquals(intersection1.t, 1.f, 1e-4);

        List<HitRecord> intersections1 = cylinder.rayIntersectionList(ray1);
        assertEquals(2, intersections1.size());

        assertTrue(intersections1.get(1).worldPoint.isClose(new Point(-1.0f, 0.0f, 0.5f)));
        assertTrue(intersections1.get(1).surfPoint.isClose(new Vec2d(0.5f, 0.5f)));
        assertTrue(intersections1.get(1).normal.isClose(new Normal(1.0f, 0.0f, 0.0f)));
        assertTrue(intersections1.get(1).ray.isClose(ray1));
        assertEquals(intersections1.get(1).t, 3.f, 1e-4);
    }

    @Test
    public void rayIntersectionTest3() throws InvalidMatrixException {
        Cylinder cylinder = new Cylinder();
        Ray ray1 = new Ray(new Point(0.0f, 0.0f, 0.5f), new Vec(1.0f, 0.0f, 0.0f));
        HitRecord intersection1 = cylinder.rayIntersection(ray1);
        assertNotNull(intersection1);
        assertTrue(intersection1.worldPoint.isClose(new Point(1.0f, 0.0f, 0.5f)));
        assertTrue(intersection1.surfPoint.isClose(new Vec2d(0.0f, 0.5f)));
        assertTrue(intersection1.normal.isClose(new Normal(-1.0f, 0.0f, 0.0f)));
        assertTrue(intersection1.ray.isClose(ray1));
        assertEquals(intersection1.t, 1.f, 1e-4);

        List<HitRecord> intersections1 = cylinder.rayIntersectionList(ray1);
        assertEquals(1, intersections1.size());


        assertTrue(cylinder.isInternal(new Point(-0.8f, 0.4f, 0.5f)));
        assertFalse(cylinder.isInternal(new Point(-1.1f, 0.0f, 0.5f)));
        assertFalse(cylinder.isInternal(new Point(-0.8f, 0.0f, 1.2f)));

        cylinder.transformation = Transformation.rotationY(90.0f);
        assertTrue(cylinder.isInternal(new Point(0.5f, 0.0f, 0.0f)));
        assertFalse(cylinder.isInternal(new Point(-0.5f, 0.0f, 0.0f)));

        cylinder.transformation = Transformation.translation(new Vec(0.5f, 0.0f, 12.0f));
        assertFalse(cylinder.isInternal(new Point(-0.8f, 0.4f, 0.5f)));
        assertTrue(cylinder.isInternal(new Point(0.7f, 0.0f, 12.5f)));
    }

    @Test
    public void rayIntersectionTest2() throws InvalidMatrixException {
        Cylinder cylinder = new Cylinder(Transformation.rotationY(90.0f));
        Ray ray1 = new Ray(new Point(0.5f, 0.0f, 2.0f), new Vec(0.0f, 0.0f, -1.0f));
        HitRecord intersection1 = cylinder.rayIntersection(ray1);
        assertNotNull(intersection1);
        assertTrue(intersection1.worldPoint.isClose(new Point(0.5f, 0.0f, 1.0f)));
        assertTrue(intersection1.surfPoint.isClose(new Vec2d(0.5f, 0.5f)));
        assertTrue(intersection1.normal.isClose(new Normal(0.0f, 0.0f, 1.0f)));
        assertTrue(intersection1.ray.isClose(ray1));
        assertEquals(intersection1.t, 1.f, 1e-4);

        List<HitRecord> intersections1 = cylinder.rayIntersectionList(ray1);

        assertTrue(intersections1.get(1).worldPoint.isClose(new Point(0.5f, 0.0f, -1.0f)));
        assertTrue(intersections1.get(1).surfPoint.isClose(new Vec2d(0.0f, 0.5f)));
        assertTrue(intersections1.get(1).normal.isClose(new Normal(0.0f, 0.0f, 1.0f)));
        assertTrue(intersections1.get(1).ray.isClose(ray1));
        assertEquals(intersections1.get(1).t, 3.f, 1e-4);
    }
}
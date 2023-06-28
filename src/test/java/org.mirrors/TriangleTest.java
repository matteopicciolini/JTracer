package org.mirrors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.*;
public class TriangleTest {
    @Test
    void rayIntersection() throws InvalidMatrixException {
        Point v0 = new Point(1, 0.5f, 0.f);
        Point v1 = new Point(-1, 0.5f, 0);
        Point v2 = new Point(0, -1, 0);
        Triangle tri = new Triangle(v0, v1, v2);
        Ray ray1 = new Ray(new Point(0.f, 0.0f, 1.f), InvVecZ);
        HitRecord intersection1 = tri.rayIntersection(ray1);

        System.out.println("prodotto scalare");
        System.out.println(ray1.dir.dot(tri.norm.toVec(), Vec.class));

        HitRecord trueIntersection1 = new HitRecord(new Point(0.f, 0.f, 0.0f),
                new Normal(0.f, 0.f, 1.f),
                new Vec2d(1.f, 0.875f),
                1.f, ray1, new Triangle(v0, v1, v2));
        assertNotNull(intersection1);
        //assertTrue(trueIntersection1.isClose(intersection1));

        Ray ray2 = new Ray(new Point(0f, 0f, 0), InvVecY);
        HitRecord intersection2 = tri.rayIntersection(ray2);
        System.out.println("raggioparallelo");

        assertNull(intersection2);
    }
    @Test
    void calculateTriangleArea(){
        Point v0 = new Point(0, 0, 3);
        Point v1 = new Point(2, 0, 3);
        Point v2 = new Point(1, 2, 3);
        Triangle tri=new Triangle(v0, v1, v2);
        System.out.println(tri.calculateTriangleArea(v0, v1, v2));
        assertEquals(tri.calculateTriangleArea(v0, v1, v2), 2f);
    }
    @Test
    void calculateSurfacePoint() throws InvalidMatrixException {
        Point v0 = new Point(0, 0, 3);
        Point v1 = new Point(3, 0, 3);
        Point v2 = new Point(0, 3, 3);
        Triangle tri=new Triangle(v0, v1, v2);

        assertTrue(tri.calculateSurfacePoint(v0).isClose(new Vec2d(0, 0)));
        assertTrue(tri.calculateSurfacePoint(v2).isClose(new Vec2d(0, 1)));
        assertTrue(tri.calculateSurfacePoint(v1).isClose(new Vec2d(1, 0)));

        Ray ray=new Ray(new Point(1, 1, 4), InvVecZ);
        assertTrue(tri.calculateSurfacePoint(tri.rayIntersection(ray).worldPoint).isClose(new Vec2d(.33333F, 0.33333F)));
    }

}


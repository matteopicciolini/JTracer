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
        Point v0 = new Point(3, 5, 3);
        Point v1 = new Point(5, 3, 3);
        Point v2 = new Point(7, 5, 3);
        Triangle tri=new Triangle(v0, v1, v2);
        System.out.println(tri.calculateTriangleArea(v0, v1, v2));
        assertEquals(tri.calculateTriangleArea(v0, v1, v2), 4f);
    }
    @Test
    void calculateSurfacePoint(){
        Point v0 = new Point(3, 0, 3);
        Point v1 = new Point(0, 3, 3);
        Point v2 = new Point(0, 0, 3);
        Triangle tri=new Triangle(v0, v1, v2);

        assertEquals(tri.calculateSurfacePoint(v0).v, 0);
        assertEquals(tri.calculateSurfacePoint(v0).u, 0);

       assertEquals(tri.calculateSurfacePoint(v1).v, 0);
       assertEquals(tri.calculateSurfacePoint(v1).u, 1);

        //assertEquals(tri.calculateSurfacePoint(v2).v, 1);
        //assertEquals(tri.calculateSurfacePoint(v2).u, 0);

        Point p=new Point(1, 1, 3);
        System.out.println(tri.calculateSurfacePoint(p).u);
        assertEquals(tri.calculateSurfacePoint(p).v, 0.333333, 1e-5);
        assertEquals(tri.calculateSurfacePoint(p).u, 0.333333, 1e-5);
    }

}


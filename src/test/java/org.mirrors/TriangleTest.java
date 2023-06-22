package org.mirrors;


import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.*;
public class TriangleTest {
    @Test
    void rayIntersection() throws InvalidMatrixException {
        Vec v0 = new Vec(1, 0.5f, 0.f);
        Vec v1 = new Vec(-1, 0.5f, 0);
        Vec v2 = new Vec(0, -1, 0);
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
}


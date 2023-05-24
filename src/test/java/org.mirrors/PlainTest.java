package org.mirrors;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class PlainTest {
    public Vec VEC_X = new Vec(1.0f, 0.0f, 0.0f);
    public Vec VEC_Y = new Vec(0.0f, 1.0f, 0.0f);
    public Vec VEC_Z = new Vec(0.0f, 0.0f, 1.0f);
    public Vec invVEC_X = new Vec(-1.0f, 0.0f, 0.0f);
    public Vec invVEC_Y = new Vec(0.0f, -1.0f, 0.0f);
    public Vec invVEC_Z = new Vec(0.0f, 0.0f, -1.0f);
    @Test
    void TestHit() throws InvalidMatrixException {
        Plain plane = new Plain();

        Ray ray1 = new Ray(new Point(0f, 0f, 1f), invVEC_Z);
        HitRecord intersection1 = plane.rayIntersection(ray1);
        assertNotNull(intersection1);
        System.out.println(1);
        assertTrue((intersection1.isClose(new HitRecord(
                new Point(0.0f, 0.0f, 0.0f),
                new Normal(0.0f, 0.0f, 1.0f),
                new Vec2d(0.0f, 0.0f),
                1.0f,
                ray1,
                plane))));


        Ray ray2 = new Ray(new Point(0f, 0f, 1f), VEC_Z);
        HitRecord intersection2 = plane.rayIntersection(ray2);
        assertNull(intersection2);

        Ray ray3 = new Ray(new Point(0f, 0f, 1f), VEC_X);
        HitRecord intersection3 = plane.rayIntersection(ray3);
        assertNull(intersection3);

        Ray ray4 = new Ray(new Point(0, 0, 1), VEC_Y);
        HitRecord intersection4 = plane.rayIntersection(ray4);
        assertNull(intersection4);


    }


        @Test
        void testTransformation() throws InvalidMatrixException {
            Plain plane = new Plain(Transformation.rotationY(90f), new Material());
            Ray ray1 = new Ray(new Point(1, 0, 0), invVEC_X);
            HitRecord intersection1 = plane.rayIntersection(ray1);
            assertNotNull(intersection1);
            assertTrue(intersection1.isClose(new HitRecord(
                    new Point(0.0f, 0.0f, 0.0f),
                    new Normal(1.0f, 0.0f, 0.0f),
                    new Vec2d(0.0f, 0.0f),
                    1.0f,
                    ray1,
                    plane)));

            Ray ray2 = new Ray(new Point(0, 0, 1), VEC_Z);
            HitRecord intersection2 = plane.rayIntersection(ray2);
            assertNull(intersection2);

            Ray ray3 = new Ray(new Point(0, 0, 1), VEC_X);
            HitRecord intersection3 = plane.rayIntersection(ray3);
            assertNull(intersection3);

            Ray ray4 = new Ray(new Point(0, 0, 1), VEC_Y);
            HitRecord intersection4 = plane.rayIntersection(ray4);
            assertNull(intersection4);
        }

        @Test
        void testUVCoordinates () throws InvalidMatrixException {
        Plain plane = new Plain();

        Ray ray1 = new Ray(new Point(0, 0, 1), invVEC_Z);
        HitRecord intersection1 = plane.rayIntersection(ray1);
        assertTrue(intersection1.surfPoint.isClose(new Vec2d(0.0f, 0.0f)));

        Ray ray2 = new Ray(new Point(0.25f, 0.75f, 1), invVEC_Z);
        HitRecord intersection2 = plane.rayIntersection(ray2);
        assertTrue(intersection2.surfPoint.isClose(new Vec2d(0.25f, 0.75f))); ;

        Ray ray3 = new Ray(new Point(4.25f, 7.75f, 1), invVEC_Z);
        HitRecord intersection3 = plane.rayIntersection(ray3);
        assertTrue(intersection3.surfPoint.isClose(new Vec2d(0.25f, 0.75f)));
    }
}

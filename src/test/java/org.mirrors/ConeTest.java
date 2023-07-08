package org.mirrors;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class ConeTest {

    @Test
    public void testHitCone() throws InvalidMatrixException {
        Cone cone = new Cone(1f, 1f);

        Transformation translation = Transformation.translation(new Vec(0f, 0f, 2f));
        Cone coneHigh = new Cone(translation, 1f, 1f);
        Ray ray = new Ray(new Point(-5f, 0f, 0f), VecX);
        HitRecord intersection = coneHigh.rayIntersection(ray);
        assertTrue(intersection == null);

        Ray ray1 = new Ray(new Point(1.5f, 0f, 0.5f), (Vec) VecX.neg());
        HitRecord intersection1 = cone.rayIntersection(ray1);
        assertTrue(intersection1 != null);
        HitRecord hit1 = new HitRecord(
                new Point(0.5f, 0f, 0.5f),
                new Normal((float) Math.sqrt(2f) / 2f, 0f, (float) Math.sqrt(2f) / 2f),
                new Vec2d(0.5f, 0.5f),
                1f,
                ray1,
                cone
        );

        assertTrue(hit1.isClose(intersection1));

        Ray ray2 = new Ray(new Point(0f, 0f, -1f), VecZ);
        HitRecord intersection2 = cone.rayIntersection(ray2);
        assertTrue(intersection2 != null);
        HitRecord hit2 = new HitRecord(
                new Point(0.0f, 0.0f, 0.0f),
                new Normal(0.0f, 0.0f, -1.0f),
                new Vec2d(0.25f, 0.25f),
                1.0f,
                ray2,
                cone
        );
        assertTrue(hit2.isClose(intersection2));

        assertTrue(cone.rayIntersection(new Ray(new Point(0f, 10f, 2f), (Vec) VecZ.neg())) == null);

        Cone cone2 = new Cone(Transformation.translation(new Vec(0f, 5f, 0f)));
        Ray ray3 = new Ray(new Point(0f, 6.5f, 0.5f), (Vec) VecY.neg());
        HitRecord intersection3 = cone2.rayIntersection(ray3);
        assertTrue(intersection3 != null);
        HitRecord hit3 = new HitRecord(
                new Point(0f, 5.5f, 0.5f),
                new Normal(0f, (float) Math.sqrt(2f) / 2f, (float) Math.sqrt(2f) / 2f),
                new Vec2d(0.625f, 0.5f),
                1f,
                ray3,
                cone2
        );

        assertTrue(hit3.isClose(intersection3));

        Ray ray4 = new Ray(new Point(0f, 6.5f, 0.0f), (Vec) VecY.neg());
        HitRecord intersection4 = cone2.rayIntersection(ray4);
        assertTrue(intersection4 != null);
        HitRecord hit4 = new HitRecord(
                new Point(0f, 6f, 0f),
                new Normal(0f, 0f, -1f),
                new Vec2d(0.25f, 0.5f),
                0.5f,
                ray4,
                cone2
        );
        assertTrue(hit4.isClose(intersection4));
    }

    @Test
    public void testInnerHitCone() {
        Cone cone = new Cone(1f, 1f);

        Ray ray1 = new Ray(new Point(0f, 0f, 0.5f), VecX);
        HitRecord intersection1 = cone.rayIntersection(ray1);
        assertTrue(intersection1 != null);
        HitRecord hit1 = new HitRecord(
                new Point(0.5f, 0.0f, 0.5f),
                new Normal(-(float) Math.sqrt(2) / 2, 0f, -(float) Math.sqrt(2) / 2),
                new Vec2d(0.5f, 0.5f),
                0.5f,
                ray1,
                cone
        );

        assertTrue(hit1.isClose(intersection1));
    }

    @Test
    public void testIsPointInside() {
        Cone cone = new Cone(0.5f, 2);
        assertTrue(cone.isInternal(new Point(0f, 0f, 1.5f)));
        assertTrue(cone.isInternal(new Point(0.25f, 0.25f, 0f)));
        assertFalse(cone.isInternal(new Point(0.5f, 0.5f, 1f)));
    }

    @Test
    public void testConeIntersectionList() {
        Cone cone = new Cone(1f, 1f);

        Ray ray = new Ray(new Point(1.5f, 0f, 0.5f), (Vec) VecX.neg());

        List<HitRecord> hits = cone.rayIntersectionList(ray);
        List<HitRecord> expected = new ArrayList<>();

        expected.add(new HitRecord(
                new Point(0.5f, 0f, 0.5f),
                new Normal((float) Math.sqrt(2f) / 2f, 0f, (float) Math.sqrt(2f) / 2f),
                new Vec2d(0.5f, 0.5f),
                1f,
                ray,
                cone)
        );
        expected.add(new HitRecord(
                new Point(-0.5f, 0f, 0.5f),
                new Normal((float) Math.sqrt(2f) / 2f, 0f, -(float) Math.sqrt(2f) / 2f),
                new Vec2d(0.75f, 0.5f),
                2f,
                ray,
                cone)
        );
        Collections.sort(expected, Comparator.comparingDouble(h -> h.t));


        assertEquals(expected.size(), hits.size());
        for (int i = 0; i < expected.size(); i++) {
            assertTrue(expected.get(i).isClose(hits.get(i)));
        }
    }
}
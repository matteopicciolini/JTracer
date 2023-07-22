package org.mirrors;

import org.junit.jupiter.api.Test;

import static java.lang.Math.pow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VecTest {
    @Test
    void vecOperation() {
        Vec a = new Vec(1.0f, 2.0f, 3.0f);
        Vec b = new Vec(4.0f, 6.0f, 8.0f);
        assertTrue(a.neg().isClose(new Vec(-1.0f, -2.0f, -3.0f)));
        assertTrue(a.sum(b).isClose(new Vec(5.0f, 8.0f, 11.0f)));
        assertTrue(b.minus(a).isClose(new Vec(3.0f, 4.0f, 5.0f)));
        assertTrue(a.dot(2.0f).isClose(new Vec(2.0f, 4.0f, 6.0f)));
        assertEquals(a.dot(b), 40.0f, 1e-5);
        assertTrue(a.cross(b).isClose(new Vec(-2.0f, 4.0f, -2.0f)));
        assertTrue(b.cross(a).isClose(new Vec(2.0f, -4.0f, 2.0f)));
        assertEquals(a.squaredNorm(), 14.0f, 1e-5);
        assertEquals(pow(a.norm(), 2), 14.0f, 1e-5);
    }
}
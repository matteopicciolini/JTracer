package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UniformPigmentTest {
    @Test
    void getColor() {
        Color color = new Color(1.f, 2.f, 3.f);
        UniformPigment pigment = new UniformPigment(color);
        assertTrue(pigment.getColor(new Vec2d(0.f, 0.f)).isClose(color));
        assertTrue(pigment.getColor(new Vec2d(1.f, 0.f)).isClose(color));
        assertTrue(pigment.getColor(new Vec2d(0.f, 1.f)).isClose(color));
        assertTrue(pigment.getColor(new Vec2d(1.f, 1.f)).isClose(color));
    }
}
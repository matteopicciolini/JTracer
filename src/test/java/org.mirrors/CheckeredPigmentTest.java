package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CheckeredPigmentTest {
    @Test
    void getColor() {
        Color color1 = new Color(1.0f, 2.0f, 3.0f);
        Color color2 = new Color(10.0f, 20.0f, 30.0f);

        CheckeredPigment pigment = new CheckeredPigment(color1, color2, 2);

        assertTrue(pigment.getColor(new Vec2d(0.25f, 0.25f)).isClose(color1));
        assertTrue(pigment.getColor(new Vec2d(0.75f, 0.25f)).isClose(color2));
        assertTrue(pigment.getColor(new Vec2d(0.25f, 0.75f)).isClose(color2));
        assertTrue(pigment.getColor(new Vec2d(0.75f, 0.75f)).isClose(color1));
    }
}
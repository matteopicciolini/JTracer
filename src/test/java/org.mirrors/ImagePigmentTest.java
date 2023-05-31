package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ImagePigmentTest {

    @Test
    void getColor() {
        HDRImage image = new HDRImage(2, 2);
        image.setPixel(0, 0, new Color(1.0f, 2.0f, 3.0f));
        image.setPixel(1, 0, new Color(2.0f, 3.0f, 1.0f));
        image.setPixel(0, 1, new Color(2.0f, 1.0f, 3.0f));
        image.setPixel(1, 1, new Color(3.0f, 2.0f, 1.0f));

        Pigment pigment = new ImagePigment(image);
        assertTrue(pigment.getColor(new Vec2d(0.f, 0.f)).isClose(new Color(1.f, 2.f, 3.f)));
        assertTrue(pigment.getColor(new Vec2d(1.f, 0.f)).isClose(new Color(2.f, 3.f, 1.f)));
        assertTrue(pigment.getColor(new Vec2d(0.f, 1.f)).isClose(new Color(2.f, 1.f, 3.f)));
        assertTrue(pigment.getColor(new Vec2d(1.f, 1.f)).isClose(new Color(3.f, 2.f, 1.f)));
    }
}
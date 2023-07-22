package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mirrors.Transformation.rotationX;
import static org.mirrors.Transformation.translation;

class RayTest {

    @Test
    void transform() throws InvalidMatrixException {
        Ray ray = new Ray(new Point(1.f, 2.f, 3.f), new Vec(6.f, 5.f, 4.f));
        Transformation transformation = translation(new Vec(10.f, 11.f, 12.f)).times(rotationX(90.f));
        Ray transformed = ray.transform(transformation);

        assertTrue(transformed.origin.isClose(new Point(11.f, 8.f, 14.f)));
        assertTrue(transformed.dir.isClose(new Vec(6.f, -4.f, 5.f)));
    }
}
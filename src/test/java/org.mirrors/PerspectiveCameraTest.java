package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PerspectiveCameraTest {

    @Test
    void fireRay() throws InvalidMatrixException {
        PerspectiveCamera cam = new PerspectiveCamera(1.f, 2.f);

        Ray ray1 = cam.fireRay(0.f, 0.f);
        Ray ray2 = cam.fireRay(1.f, 0.f);
        Ray ray3 = cam.fireRay(0.f, 1.f);
        Ray ray4 = cam.fireRay(1.f, 1.f);

        assertTrue(ray1.origin.isClose(ray2.origin));
        assertTrue(ray1.origin.isClose(ray3.origin));
        assertTrue(ray1.origin.isClose(ray4.origin));

        assertTrue(ray1.at(1.f).isClose(new Point(0.f, 2.f, -1.f)));
        assertTrue(ray2.at(1.f).isClose(new Point(0.f, -2.f, -1.f)));
        assertTrue(ray3.at(1.f).isClose(new Point(0.f, 2.f, 1.f)));
        assertTrue(ray4.at(1.f).isClose(new Point(0.f, -2.f, 1.f)));
    }
}
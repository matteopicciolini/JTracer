package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrthogonalCameraTest {
    @Test
    void fireRay() throws InvalidMatrixException {
        OrthogonalCamera cam = new OrthogonalCamera(2.f);

        Ray ray1 = cam.fireRay(0.f, 0.f);
        Ray ray2 = cam.fireRay(1.f, 0.f);
        Ray ray3 = cam.fireRay(0.f, 1.f);
        Ray ray4 = cam .fireRay(1.f, 1.f);

        assertEquals(0.f, ray1.dir.cross(ray2.dir).squaredNorm(), 1e-5);
        assertEquals(0.f, ray1.dir.cross(ray3.dir).squaredNorm(), 1e-5);
        assertEquals(0.f, ray1.dir.cross(ray4.dir).squaredNorm(), 1e-5);

        assertTrue(ray1.at(1.f).isClose(new Point(0.f, 2.f, -1.f)));
        assertTrue(ray2.at(1.f).isClose(new Point(0.f, -2.f, -1.f)));
        assertTrue(ray3.at(1.f).isClose(new Point(0.f, 2.f, 1.f)));
        assertTrue(ray4.at(1.f).isClose(new Point(0.f, -2.f, 1.f)));
    }
}
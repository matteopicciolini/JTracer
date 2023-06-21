package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ONBTest {
    @Test
    void testOnbFromNormal(){
        PCG pcg = new PCG();

        for (int i = 0; i < 100; ++i){
            Normal normal = new Normal(pcg.randomFloat(), pcg.randomFloat(), pcg.randomFloat());
            normal.normalize();

            ONB onb = new ONB(normal);
            assertTrue(onb.e3.isClose(normal));

            assertEquals(onb.e1.squaredNorm(), 1.f, 1e-5);
            assertEquals(onb.e2.squaredNorm(), 1.f, 1e-5);
            assertEquals(onb.e3.squaredNorm(), 1.f, 1e-5);

            assertEquals(onb.e1.dot(onb.e2, other, Vec.class), 0.f, 1e-5);
            assertEquals(onb.e2.dot(onb.e3, other, Vec.class), 0.f, 1e-5);
            assertEquals(onb.e3.dot(onb.e1, other, Vec.class), 0.f, 1e-5);
        }
    }
}
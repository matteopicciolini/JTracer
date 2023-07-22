package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PCGTest {
    @Test
    void testRandom() {
        PCG pcg = new PCG(42, 54);

        assertEquals(pcg.state, 1753877967969059832L);
        assertEquals(pcg.inc, 109L);

        long[] correctList = new long[]{2707161783L, 2068313097L, 3122475824L, 2211639955L, 3215226955L, 3421331566L};
        for (int i = 0; i < 6; ++i) {
            assertEquals(correctList[i], pcg.random());
        }

    }
}
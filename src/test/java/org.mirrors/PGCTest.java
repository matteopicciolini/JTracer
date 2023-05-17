package org.mirrors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class PGCTest {
    @Test
    void PGCRand() {
        long [] l = {2707161783l, 2068313097l,
                3122475824l, 2211639955l,
                3215226955l, 3421331566l};
        PCG pcg= new PCG(42, 54);
        assertEquals(pcg.state, 1753877967969059832l);
        for(int i =0; i<6; i++){

            assertEquals(pcg.random(), l[i]);
        }
    }

}
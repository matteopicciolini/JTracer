package org.mirrors;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Matrix4x4Test {

    @Test
    void cross() throws InvalidMatrix {
        Matrix4x4 a = new Matrix4x4(new float[]{
                1.0f, 2.0f, 3.0f, 4.0f,
                5.0f, 6.0f, 7.0f, 8.0f,
                9.0f, 10.0f, 11.0f, 12.0f,
                13.0f, 14.0f, 15.0f, 16.0f});
        Matrix4x4 b = new Matrix4x4(new float[]{
                4.0f, 3.0f, 2.0f, 1.0f,
                8.0f, 7.0f, 6.0f, 5.0f,
                12.0f, 11.0f, 10.0f, 9.0f,
                16.0f, 15.0f, 14.0f, 13.0f});
        assertTrue(
                a.cross(b).isClose(new Matrix4x4(new float[]{
                120.0f, 110.0f, 100.0f, 90.0f,
                280.0f, 254.0f, 228.0f, 202.0f,
                440.0f, 398.0f, 356.0f, 314.0f,
                600.0f, 542.0f, 484.0f, 426.0f}))
        );
    }
}
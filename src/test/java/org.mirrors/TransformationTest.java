package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransformationTest {

    @Test
    void inverse() throws InvalidMatrixException {
        Matrix4x4 matrix = new Matrix4x4(new float[]{
                1.0f, 2.0f, 3.0f, 4.0f,
                5.0f, 6.0f, 7.0f, 8.0f,
                9.0f, 9.0f, 8.0f, 7.0f,
                6.0f, 5.0f, 4.0f, 1.0f});
        Matrix4x4 invMatrix = new Matrix4x4(new float[]{
                -3.75f, 2.75f, -1.0f, 0.0f,
                4.375f, -3.875f, 2.0f, -0.5f,
                0.5f, 0.5f, -1.0f, 1.0f,
                -1.375f, 0.875f, 0.0f, -0.5f,
        });
        Transformation t1  = new Transformation(matrix, invMatrix);

        Transformation t2 = t1.inverse();
        assertTrue(t2.isConsistent());

        Transformation prod = t1.times(t2);

        assertTrue(prod.isConsistent());
        assertTrue(prod.isClose(new Transformation())); // M*M^-1 = ID
    }

    @Test
    void translation() throws InvalidMatrixException {
        Transformation tr1 = Transformation.translation(new Vec(1.0f, 2.0f, 3.0f));
        assertTrue(tr1.isConsistent());

        Transformation tr2 = Transformation.translation(new Vec(4.0f, 6.0f, 8.0f));
        assertTrue(tr2.isConsistent());

        Transformation expected = Transformation.translation(new Vec(5.0f, 8.0f, 11.0f));
        Transformation prod = tr1.times(tr2);
        assertTrue(prod.isConsistent());
        assertTrue(expected.isClose(prod));
    }

    @Test
    void scaling() throws InvalidMatrixException {
        Transformation tr1 = Transformation.scaling(new Vec(2.0f, 5.0f, 10.0f));
        assertTrue(tr1.isConsistent());

        Transformation tr2 = Transformation.scaling(new Vec(3.0f, 2.0f, 4.0f));
        assertTrue(tr2.isConsistent());

        Transformation expected = Transformation.scaling(new Vec(6.0f, 10.0f, 40.0f));
        Transformation prod = tr1.times(tr2);
        assertTrue(prod.isConsistent());
        assertTrue(expected.isClose(prod));
    }

    @Test
    void testTimes() throws InvalidMatrixException {
        Transformation t = new Transformation(
                new Matrix4x4(new float[]{
                    1.0f, 2.0f, 3.0f, 4.0f,
                    5.0f, 6.0f, 7.0f, 8.0f,
                    9.0f, 9.0f, 8.0f, 7.0f,
                    0.0f, 0.0f, 0.0f, 1.0f
                }),
                new Matrix4x4((new float[]{
                    -3.75f, 2.75f, -1f, 0f,
                    5.75f, -4.75f, 2.0f, 1.0f,
                    -2.25f, 2.25f, -1.0f, -2.0f,
                    0.0f, 0.0f, 0.0f, 1.0f
                }))
        );

        assertTrue(t.isConsistent());

        Vec expectedV = new Vec(14.0f, 38.0f, 51.0f);
        assertTrue(expectedV.isClose(t.times(new Vec(1.0f, 2.0f, 3.0f))));

        Point expectedP = new Point(18.0f, 46.0f, 58.0f);
        assertTrue(expectedP.isClose(t.times(new Point(1.0f, 2.0f, 3.0f))));

        Normal expectedN = new Normal(-8.75f, 7.75f, -3.0f);
        assertTrue(expectedN.isClose(t.times(new Normal(3.0f, 2.0f, 4.0f))));
    }

    @Test
    void isClose() throws InvalidMatrixException {
        Transformation t1 = new Transformation(
                new Matrix4x4(new float[]{
                        1.0f, 2.0f, 3.0f, 4.0f,
                        5.0f, 6.0f, 7.0f, 8.0f,
                        9.0f, 9.0f, 8.0f, 7.0f,
                        6.0f, 5.0f, 4.0f, 1.0f
                }),
                new Matrix4x4(new float[]{
                        -3.75f, 2.75f, -1.0f, 0.0f,
                        4.375f, -3.875f, 2.0f, -0.5f,
                        0.5f, 0.5f, -1.0f, 1.0f,
                        -1.375f, 0.875f, 0.0f, -0.5f
                })
        );
        assertTrue(t1.isConsistent());

        Transformation t2 = new Transformation(
                new Matrix4x4(new float[]{
                        1.0f, 2.0f, 3.0f, 4.0f,
                        5.0f, 6.0f, 7.0f, 8.0f,
                        9.0f, 9.0f, 8.0f, 7.0f,
                        6.0f, 5.0f, 4.0f, 1.0f
                }),
                new Matrix4x4(new float[]{
                        -3.75f, 2.75f, -1.0f, 0.0f,
                        4.375f, -3.875f, 2.0f, -0.5f,
                        0.5f, 0.5f, -1.0f, 1.0f,
                        -1.375f, 0.875f, 0.0f, -0.5f
                })
        );
        assertTrue(t1.isClose(t2));

        Transformation t3 = new Transformation(
                new Matrix4x4(new float[]{
                        1.0f, 2.0f, 3.0f, 4.0f,
                        5.0f, 6.0f, 7.0f, 8.0f,
                        9.0f, 9.0f, 9.0f, 7.0f,
                        6.0f, 5.0f, 4.0f, 1.0f
                }),
                new Matrix4x4(new float[]{
                        -3.75f, 2.75f, -1.0f, 0.0f,
                        4.375f, -3.875f, 2.0f, -0.5f,
                        0.5f, 0.5f, -1.0f, 1.0f,
                        -1.375f, 0.875f, 0.0f, -0.5f
                })
        );
        assertFalse(t3.isClose(t1));
    }

    @Test
    void rotation() throws InvalidMatrixException {
        assertTrue(Transformation.rotationX(30.0f).isConsistent());
        assertTrue(Transformation.rotationY(30.0f).isConsistent());
        assertTrue(Transformation.rotationZ(30.0f).isConsistent());

        Vec v1 = (Vec) Transformation.rotationX(180.0f).times(new Vec(1.0f, 2.0f, 3.0f));
        assertTrue(v1.isClose(new Vec(1.0f, -2.0f, -3.0f)));

        Vec v2 = (Vec) (Transformation.rotationY(180.0f)).times(new Vec(1.0f, 2.0f, 3.0f));
        assertTrue(v2.isClose(new Vec(-1.0f, 2.0f, -3.0f)));

        Vec v3 = (Vec) (Transformation.rotationZ(180.0f)).times(new Vec(1.0f, 2.0f, 3.0f));
        assertTrue(v3.isClose(new Vec(-1.0f, -2.0f, 3.0f)));
    }


    @Test
    void multiplication() throws InvalidMatrixException {
        Transformation t1 = new Transformation(
                new Matrix4x4(new float[]{
                        1.0f, 2.0f, 3.0f, 4.0f,
                        5.0f, 6.0f, 7.0f, 8.0f,
                        9.0f, 9.0f, 8.0f, 7.0f,
                        6.0f, 5.0f, 4.0f, 1.0f,
                }),
                new Matrix4x4(new float[]{
                        -3.75f, 2.75f, -1, 0,
                        4.375f, -3.875f, 2.0f, -0.5f,
                        0.5f, 0.5f, -1.0f, 1.0f,
                        -1.375f, 0.875f, 0.0f, -0.5f
                }));
        assertTrue(t1.isConsistent());

        Transformation t2 = new Transformation(
                new Matrix4x4(new float[]{
                        3.0f, 5.0f, 2.0f, 4.0f,
                        4.0f, 1.0f, 0.0f, 5.0f,
                        6.0f, 3.0f, 2.0f, 0.0f,
                        1.0f, 4.0f, 2.0f, 1.0f,
                }),
                new Matrix4x4(new float[]{
                        0.4f, -0.2f, 0.2f, -0.6f,
                        2.9f, -1.7f, 0.2f, -3.1f,
                        -5.55f, 3.15f, -0.4f, 6.45f,
                        -0.9f, 0.7f, -0.2f, 1.1f,
                }));
        assertTrue(t2.isConsistent());

        Transformation expected = new Transformation(
                new Matrix4x4(new float[]{
                        33.0f, 32.0f, 16.0f, 18.0f,
                        89.0f, 84.0f, 40.0f, 58.0f,
                        118.0f, 106.0f, 48.0f, 88.0f,
                        63.0f, 51.0f, 22.0f, 50.0f
                }),
                new Matrix4x4(new float[]{
                        -1.45f, 1.45f, -1.0f, 0.6f,
                        -13.95f, 11.95f, -6.5f, 2.6f,
                        25.525f, -22.025f, 12.25f, -5.2f,
                        4.825f, -4.325f, 2.5f, -1.1f
                }));

        assertTrue(expected.isConsistent());
        Transformation prod = t1.times(t2);

        assertTrue(expected.matrix.isClose(prod.matrix));
        assertTrue(expected.invMatrix.isClose(prod.invMatrix));
    }
}
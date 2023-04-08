package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransformationTest {

    @Test
    void isConsistent() {
    }

    @Test
    void inverse() throws InvalidMatrix {
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
    void translation() throws InvalidMatrix {
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
    void scaling() throws InvalidMatrix {
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
    void testTimes() throws InvalidMatrix {
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
        System.out.println(t.times(new Normal(3.0f, 2.0f, 4.0f)));
        //assertTrue(expectedN.isClose(t.times(new Normal(3.0f, 2.0f, 4.0f))));
    }

    @Test
    void isClose() throws InvalidMatrix {
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
    void rotation() throws InvalidMatrix {

        assertTrue(Transformation.rotation_x(30.0f).isConsistent());
        assertTrue(Transformation.rotation_y(30.0f).isConsistent());
        assertTrue(Transformation.rotation_z(30.0f).isConsistent());

        Vec v1= (Vec) (Transformation.rotation_x(180.0f)).times(new Vec(1.0f, 2.0f, 3.0f));
        assertTrue(v1.isClose(new Vec(1.0f, -2.0f, -3.0f)));

        Vec v2= (Vec) (Transformation.rotation_y(180.0f)).times(new Vec(1.0f, 2.0f, 3.0f));
        assertTrue(v2.isClose(new Vec(-1.0f, 2.0f, -3.0f)));

        Vec v3= (Vec) (Transformation.rotation_z(180.0f)).times(new Vec(1.0f, 2.0f, 3.0f));
        assertTrue(v3.isClose(new Vec(-1.0f, -2.0f, 3.0f)));

    }

}
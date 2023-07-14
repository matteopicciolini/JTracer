package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.Black;
import static org.mirrors.Global.White;

class PathTracerTest {

    @Test
    void testFurnace() throws InvalidMatrixException {
        PCG pcg = new PCG();
        for (int i = 0; i < 15; ++i){
            World world = new World();


            float reflectanceFloat = pcg.randomFloat();
            float emittedRadianceFloat = pcg.randomFloat() * 0.9f;

            BRDF reflectance = new DiffuseBRDF(new UniformPigment(White.prod(reflectanceFloat)));
            Pigment emittedRadiance = new UniformPigment(White.prod(emittedRadianceFloat));
            Material enclosureMaterial = new Material (reflectance, emittedRadiance);

            world.addShape(new Sphere(enclosureMaterial));

            PathTracer pathTracer = new PathTracer(world, Black, pcg, 1, 100, 101);
            Ray ray = new Ray(new Point(0.f, 0.f, 0.f), new Vec(1.f, 0.f, 0.f));
            Color color = pathTracer.call(ray);

            float colorExpected = emittedRadianceFloat / (1.f - reflectanceFloat);

            System.out.println(colorExpected-color.r);
            assertEquals(colorExpected, color.r, 1e-3);
            assertEquals(colorExpected, color.g, 1e-3);
            assertEquals(colorExpected, color.b, 1e-3);
        }
    }
}
package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mirrors.Global.Black;

class FlatRendererTest {

    @Test
    void flatRendererTest() throws InvalidMatrixException {
        Color sphereColor = new Color(1.f, 2.f, 3.f);
        Transformation translation = Transformation.translation(new Vec(2.f, 0.f, 0.f));
        Transformation scale = Transformation.scaling(new Vec(0.2f, 0.2f, 0.2f));
        Material material = new Material(new DiffuseBRDF(new UniformPigment(sphereColor), 1.f));
        Sphere sphere = new Sphere(translation.times(scale), material);

        HDRImage image = new HDRImage(3, 3);
        OrthogonalCamera camera = new OrthogonalCamera(1.f);
        ImageTracer tracer = new ImageTracer(image, camera);
        World world = new World();
        world.addShape(sphere);
        FlatRenderer renderer = new FlatRenderer(world);
        tracer.fireAllRays(renderer, 10);

        assertTrue(image.getPixel(0, 0).isClose(Black));
        assertTrue(image.getPixel(1, 0).isClose(Black));
        assertTrue(image.getPixel(2, 0).isClose(Black));

        assertTrue(image.getPixel(0, 1).isClose(Black));
        assertTrue(image.getPixel(1, 1).isClose(sphereColor));
        assertTrue(image.getPixel(2, 1).isClose(Black));

        assertTrue(image.getPixel(0, 2).isClose(Black));
        assertTrue(image.getPixel(1, 2).isClose(Black));
        assertTrue(image.getPixel(2, 2).isClose(Black));

    }
}
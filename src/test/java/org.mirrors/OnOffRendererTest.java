package org.mirrors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mirrors.Global.Black;
import static org.mirrors.Global.White;
import static org.mirrors.Transformation.scaling;

class OnOffRendererTest {
    @Test
    void onOffRendererTest() throws InvalidMatrixException {
        Transformation translation = Transformation.translation(new Vec(2.f, 0.f, 0.f));
        Transformation scale = scaling(new Vec(0.2f, 0.2f, 0.2f));
        Material sphereMaterial = new Material(new DiffuseBRDF(new UniformPigment(White), 1.f));
        Sphere sphere = new Sphere(translation.times(scale), sphereMaterial);

        HDRImage image = new HDRImage(3, 3);
        OrthogonalCamera camera = new OrthogonalCamera(1.f);
        ImageTracer tracer = new ImageTracer(image, camera);
        World world = new World();
        world.addShape(sphere);
        OnOffRenderer renderer = new OnOffRenderer(world);
        tracer.fireAllRays(renderer);

        assertTrue(image.getPixel(0, 0).isClose(Black));
        assertTrue(image.getPixel(1, 0).isClose(Black));
        assertTrue(image.getPixel(2, 0).isClose(Black));

        assertTrue(image.getPixel(0, 1).isClose(Black));
        assertTrue(image.getPixel(1, 1).isClose(White));
        assertTrue(image.getPixel(2, 1).isClose(Black));

        assertTrue(image.getPixel(0, 2).isClose(Black));
        assertTrue(image.getPixel(1, 2).isClose(Black));
        assertTrue(image.getPixel(2, 2).isClose(Black));
    }

}
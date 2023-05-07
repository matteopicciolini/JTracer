package org.mirrors;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ImageTracerTest {
    HDRImage image;
    PerspectiveCamera camera;
    ImageTracer tracer;

    @BeforeEach
    void setUp(){
        image = new HDRImage(4, 2);
        camera = new PerspectiveCamera(1.f,2.f);
        tracer = new ImageTracer(image, camera);
    }

    @Test
    void testOrientation() throws InvalidMatrixException {
        Ray topLeftRay = tracer.fireRay(0, 0, 0.f, 0.f);
        assertTrue((new Point(0.f, 2.f, 1.f)).isClose(topLeftRay.at(1.f)));

        Ray bottomRightRay =tracer.fireRay(3, 1, 1.f, 1.f);
        assertTrue((new Point(0.f, -2.f, -1.f)).isClose(bottomRightRay.at(1.f)));
    }

    @Test
    void testUvSubMapping(){
        Ray ray1 = tracer.fireRay(0, 0, 2.5f, 1.5f);
        Ray ray2 = tracer.fireRay(2, 1, 0.5f, 0.5f);
        assertTrue(ray1.isClose(ray2));
    }

    @Test
    void testImageCoverage(){
        tracer.fireAllRays((ray) -> new Color(1.f, 2.f, 3.f));
        for(int i = 0; i < this.image.height; ++i){
            for(int j = 0; j < this.image.width; ++j){
                assertTrue(this.image.getPixel(j, i).isClose(new Color(1.f, 2.f, 3.f)));
            }
        }
    }
}
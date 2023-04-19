package org.mirrors;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class imageTracerTest {

    @Test
    void ImageTracer() {


        HDRImage image = new HDRImage(4, 2);
        PerspectioveCamera cam = new PerspectioveCamera(1, 2);
        imageTracer tracer = new imageTracer(image, cam);
        Ray ray1 = tracer.fire_ray(0, 0, 2.5f, 1.5f);
        Ray ray2 = tracer.fire_ray(2, 1, 0.5f, 0.5f);

        assertTrue(ray1.isClose(ray2));

        tracer.fire_all_rays();
        for (int i = 0; i < image.width; i++) {
            for (int j = 0; j < image.height; j++) {

                assertTrue( image.getPixel(i, j).isClose( new Color(1.0f, 2.0f, 3.0f)));
            }
        }
    }
}
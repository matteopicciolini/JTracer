package org.mirrors;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import static java.lang.Math.pow;
import static org.mirrors.Global.Black;
import static org.mirrors.Global.White;

/**
 A class that performs ray tracing on an HDR image using a camera object.
 */
public class ImageTracer {
    public HDRImage image;
    public Camera camera;
    public int samplesPerSide = 0;
    public PCG pcg;

    /**
     Constructor for the ImageTracer class.
     @param image the HDR image to be traced.
     @param camera the camera object used for ray tracing.
     */
    public ImageTracer(HDRImage image, Camera camera){
        this.image = image;
        this.camera = camera;
    }

    public ImageTracer(HDRImage image, Camera camera, int samplesPerSide, PCG pcg){
        this.image = image;
        this.camera = camera;
        this.samplesPerSide = samplesPerSide;
        this.pcg = pcg;
    }
    /**
     Fire a ray from the camera through a pixel in the image.
     @param col the column index of the pixel in the image.
     @param row the row index of the pixel in the image.
     @param uPixel the x offset of the pixel center from the pixel's top-left corner, in pixels.
     @param vPixel the y offset of the pixel center from the pixel's top-left corner, in pixels.
     @return the ray that passes through the specified pixel.
     */
    public Ray fireRay(int col, int row, float uPixel, float vPixel){
        float u = (col + uPixel) / this.image.width;
        float v = 1.f - (row + vPixel) / this.image.height;
        return this.camera.fireRay(u, v);
    }

    /**
     Fire a ray for each pixel in the image and set its color according to the returned value from a given function.
     */
    public void fireAllRays(RayToColor f) throws InvalidMatrixException {
        for(int i = 0; i < this.image.width; ++i){
            for(int j = 0; j < this.image.height; ++j){
                Color cumColor = Black;
                if (this.samplesPerSide > 0) { //Antialiasing
                    for (int interPixelRow = 0; interPixelRow < this.samplesPerSide; ++interPixelRow){
                        for (int interPixelCol = 0; interPixelCol < this.samplesPerSide; ++interPixelCol){
                            float uPixel = (interPixelCol + this.pcg.randomFloat()) / this.samplesPerSide;
                            float vPixel = (interPixelRow + this.pcg.randomFloat()) / this.samplesPerSide;
                            Ray ray = this.fireRay(i, j, uPixel, vPixel);
                            cumColor = cumColor.sum(f.call(ray));
                        }
                    }
                    this.image.setPixel(i, j, cumColor.prod (1f / (float) pow(this.samplesPerSide, 2)));
                }
                else {
                    Ray ray = this.fireRay(i, j, 0.5f, 0.5f);
                    Color color = f.call(ray);
                    this.image.setPixel(i, j, color);
                }
            }
        }
    }


    public void fireAllRaysParallel(RayToColor f, int numThreads) throws InvalidMatrixException {
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        AtomicInteger progress = new AtomicInteger(0);
        int totalPixels = this.image.width * this.image.height;
        int theadProgress[] =  {0, 0, 0, 0};

        for (int i = 0; i < this.image.width; ++i) {
            for (int j = 0; j < this.image.height; ++j) {
                final int pixelX = i;
                final int pixelY = j;

                executor.execute(() -> {
                    Color cumColor = Black;
                    if (this.samplesPerSide > 0) { // Antialiasing
                        for (int interPixelRow = 0; interPixelRow < this.samplesPerSide; ++interPixelRow) {
                            for (int interPixelCol = 0; interPixelCol < this.samplesPerSide; ++interPixelCol) {
                                float uPixel = (interPixelCol + this.pcg.randomFloat()) / this.samplesPerSide;
                                float vPixel = (interPixelRow + this.pcg.randomFloat()) / this.samplesPerSide;
                                Ray ray = this.fireRay(pixelX, pixelY, uPixel, vPixel);
                                try {
                                    cumColor = cumColor.sum(f.call(ray));
                                } catch (InvalidMatrixException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                        this.image.setPixel(pixelX, pixelY, cumColor.prod(1f / (float) Math.pow(this.samplesPerSide, 2)));
                    } else {
                        Ray ray = this.fireRay(pixelX, pixelY, 0.5f, 0.5f);
                        Color color = null;
                        try {
                            color = f.call(ray);
                        } catch (InvalidMatrixException e) {
                            throw new RuntimeException(e);
                        }
                        this.image.setPixel(pixelX, pixelY, color);
                    }


                    int completedPixels = progress.incrementAndGet();
                    int percentComplete = (int) (((float) completedPixels / totalPixels) * 100);
                    System.out.print("\rThread " + Thread.currentThread().getName()+ " - Progress: " + percentComplete + "%");
                });
            }
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            // Aspetta la fine dell'esecuzione di tutti i thread
        }
    }


}

package org.mirrors;

/**
 A class that performs ray tracing on an HDR image using a camera object.
 */
public class ImageTracer {
    public HDRImage image;
    public Camera camera;


    /**
     Constructor for the ImageTracer class.
     @param image the HDR image to be traced.
     @param camera the camera object used for ray tracing.
     */
    public ImageTracer(HDRImage image, Camera camera){
        this.image = image;
        this.camera = camera;
    }

    /**
     Fire a ray from the camera through a pixel in the image.
     @param col the column index of the pixel in the image.
     @param row the row index of the pixel in the image.
     @param u_pixel the x offset of the pixel center from the pixel's top-left corner, in pixels.
     @param v_pixel the y offset of the pixel center from the pixel's top-left corner, in pixels.
     @return the ray that passes through the specified pixel.
     */
    public Ray fire_ray(int col, int row, float u_pixel, float v_pixel){
        float u = (col + u_pixel) / (this.image.width - 1);
        float v = (row + v_pixel) / (this.image.height - 1);
        return this.camera.fireRay(u, v);
    }

    /**
     Fire a ray for each pixel in the image and set its color according to the returned value from a given function.
     */
    public void fire_all_rays(){
        myfunc f = new myfunc();
        for(int i = 0; i < this.image.width; ++i){
            for(int j = 0; j < this.image.height; ++j){
                Ray ray = this.fire_ray(i, j, 0.5f, 0.5f);
                Color color = f.func(ray);
                this.image.setPixel(i, j, color);
            }
        }
    }
}

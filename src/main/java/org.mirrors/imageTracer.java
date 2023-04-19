package org.mirrors;

public class imageTracer {
    public HDRImage image;
    public Camera camera;
    public imageTracer(HDRImage image, Camera camera){
        this.image=image;
        this.camera=camera;
    }
    public Ray fire_ray(int col, int row, float u_pixel, float v_pixel){
        float u = (col + u_pixel) / (this.image.width - 1);
        float v = (row + v_pixel) / (this.image.height - 1);
        Ray ray1=new Ray();
        return ;
    }
}

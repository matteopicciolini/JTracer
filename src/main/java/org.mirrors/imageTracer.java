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
        return this.camera.fireRay(u, v);
    }

    public void fire_all_rays(){
        myfunc f=new myfunc();
        for(int i =0; i<this.image.width; i++){
            for(int j=0; j<this.image.height; j++){
                Ray ray=this.fire_ray(i, j, 0.5f, 0.5f);

                Color color=f.func(ray);
                this.image.setPixel(i, j, color);
            }
        }
    }
}

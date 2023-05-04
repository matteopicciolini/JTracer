package org.mirrors;

public class HitRecord {
    public Point world_point;
    public Normal normal;
    public float[] surf_point;
    public float t;
    public Ray ray;

    public HitRecord(){

        this.world_point=null;
        this.normal=null;
        this.surf_point= new float[]{Float.parseFloat(null)};
        this.t= Float.parseFloat(null);
        this.ray=null;


    }
    public HitRecord(Point w_p, Normal norm, float s_p, float t, Ray ray){

        this.world_point=w_p;
        this.normal=norm;
        this.surf_point= new float[]{s_p};
        this.t=t;
        this.ray=ray;


    }
}

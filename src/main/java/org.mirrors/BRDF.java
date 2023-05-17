package org.mirrors;

public abstract class BRDF {
    public Pigment pigment;

    public BRDF(Pigment pigment){
        this.pigment = pigment;
    }
    public abstract Color eval(Normal norm, Vec ind_dir, Vec2d uv);
}

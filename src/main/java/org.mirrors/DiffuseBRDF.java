package org.mirrors;

public class DiffuseBRDF extends BRDF {
    public DiffuseBRDF(Pigment pig, float reflectance){
        this.pig=pig;
        this.reflectance=reflectance;
    }
    public Color eval(Normal norm, Vec dir, Vec2d uv){
        return this.pig.get_color(uv) * (this.reflectance / Math.PI);
    }
}

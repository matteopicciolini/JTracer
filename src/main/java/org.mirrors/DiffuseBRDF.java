package org.mirrors;

import static org.mirrors.Global.Black;

public class DiffuseBRDF extends BRDF {
    float reflectance;
    public DiffuseBRDF(Pigment pigment, float reflectance){
        super(pigment);
        this.reflectance = reflectance;
    }
    public DiffuseBRDF(){
        super(new UniformPigment(Black));
        this.reflectance = 1.f;
    }
    public Color eval(Normal norm, Vec dir, Vec2d uv){
        return this.pigment.getColor(uv).prod((float) (this.reflectance / Math.PI));
    }
}

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

    public Ray scatter_ray(PCG pcg, Vec incomingDir, Point interactionPoint,  Normal normal, int depth){
        ONB onb = new ONB(normal);
        float cos_theta_sq = pcg.random_float();
        float cos_theta= (float) Math.sqrt(cos_theta_sq);
        float sin_theta = (float) Math.sqrt(1.0 - cos_theta_sq);
        float phi = (float) (2.0 * Math.PI * pcg.random_float());
        float sincos = (float) Math.sin(phi)*(cos_theta);
        Vec e1 = (Vec) onb.e3.dot(sin_theta);
        Vec e2= (Vec) onb.e2.dot(sincos);
        Vec Sum =e2.sum(e1);
        float ris=1f * (float) Math.cos(phi) * cos_theta;
        return new Ray(interactionPoint,
                Sum.sum(ris),
                depth);
    }
}

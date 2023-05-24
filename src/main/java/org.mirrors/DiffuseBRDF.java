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

        return Ray(interactionPoint,
                1 * Math.cos(phi) * cos_theta + onb.e2 * Math.sin(phi) * cos_theta + onb.e3 * sin_theta,
                1.0e-3,
                Float.POSITIVE_INFINITY,
                depth);
}
}

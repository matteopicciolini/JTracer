package org.mirrors;

import static org.mirrors.Global.Black;
import static org.mirrors.Global.White;

public class DiffuseBRDF extends BRDF {

    public DiffuseBRDF(Pigment pigment){
        super(pigment);
    }
    public DiffuseBRDF(Pigment pigment, float reflectance){
        super(pigment);
    }
    public DiffuseBRDF(){
        super(new UniformPigment(White));
    }

    @Override
    public Color eval(Normal norm, Vec dir, Vec outDir, Vec2d uv){
        return this.pigment.getColor(uv).prod((float) (1.f / Math.PI));
    }

    @Override
    public Ray scatterRay(PCG pcg, Vec incomingDir, Point interactionPoint, Normal normal, int depth){
        ONB onb = new ONB(normal);
        float cosThetaSq = pcg.randomFloat();
        float cosTheta = (float) Math.sqrt(cosThetaSq);
        float sinTheta = (float) Math.sqrt(1.0f - cosThetaSq);
        float phi = (float) (2.0f * Math.PI * pcg.randomFloat());

        Vec factor1 = (Vec) onb.e1.dot((float) Math.cos(phi) * cosTheta);
        Vec factor2 = (Vec) onb.e2.dot((float) Math.sin(phi) * cosTheta);
        Vec factor3 = (Vec) onb.e3.dot(sinTheta);

        return new Ray(interactionPoint, factor1.sum(factor2).sum(factor3),
                1.e-3f, Float.POSITIVE_INFINITY, depth);
    }
}

package org.mirrors;

public abstract class BRDF {
    public Pigment pigment;

    public BRDF(Pigment pigment){
        this.pigment = pigment;
    }
    public abstract Color eval(Normal norm, Vec indDir, Vec outDir, Vec2d uv);

    public abstract Ray scatterRay(PCG pcg, Vec incomingDir, Point interactionPoint, Normal normal, int depth);
}

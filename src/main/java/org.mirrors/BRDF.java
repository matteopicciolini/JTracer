package org.mirrors;

import static org.mirrors.Global.Black;
import static org.mirrors.Global.White;

public abstract class BRDF {
    public Pigment pigment;

    public BRDF(){
        this.pigment = new UniformPigment(White);
    }
    public BRDF(Pigment pigment){
        this.pigment = pigment;
    }

    public Color eval(Normal norm, Vec indDir, Vec outDir, Vec2d uv){
        return Black;
    };

    public abstract Ray scatterRay(PCG pcg, Vec incomingDir, Point interactionPoint, Normal normal, int depth);

}

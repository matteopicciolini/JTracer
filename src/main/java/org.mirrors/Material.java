package org.mirrors;

import static org.mirrors.Global.Black;

public class Material {
    public BRDF brdf;
    public Pigment emittedRadiance;

    public Material(){
        this.brdf = new DiffuseBRDF();
        this.emittedRadiance = new UniformPigment(Black);
    }

    public Material(BRDF brdf) {
        this.brdf = brdf;
        this.emittedRadiance = new UniformPigment(Black);
    }

    public Material(BRDF brdf, Pigment emittedRadiance) {
        this.brdf = brdf;
        this.emittedRadiance = emittedRadiance;
    }

}

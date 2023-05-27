package org.mirrors;

public class Material {
    BRDF brdf;
    Pigment emittedRadiance;

    public Material(){
        this.brdf = new DiffuseBRDF();
        this.emittedRadiance = new UniformPigment(new Color(0.f, 0.f, 0.f));
    }

    public Material(BRDF brdf) {
        this.brdf = brdf;
        this.emittedRadiance = new UniformPigment(new Color(0.f, 0.f, 0.f));
    }

    public Material(BRDF brdf, Pigment emittedRadiance) {
        this.brdf = brdf;
        this.emittedRadiance = emittedRadiance;
    }

}

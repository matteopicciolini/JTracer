package org.mirrors;

public class Material {
    BRDF brdf;
    Pigment emittedRadiance = new UniformPigment(new Color(0.f, 0.f, 0.f));

    public Material(){
        this.brdf = new DiffuseBRDF() ;
    }

    public Material(BRDF brdf) {
        this.brdf = brdf;
    }
}

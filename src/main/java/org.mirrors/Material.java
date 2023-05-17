package org.mirrors;

public class Material {
    BRDF brdf = new DiffuseBRDF();
    Pigment emittedRadiance = new UniformPigment(new Color(0.f, 0.f, 0.f));
}

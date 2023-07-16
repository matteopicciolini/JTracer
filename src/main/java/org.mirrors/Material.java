package org.mirrors;

import static org.mirrors.Global.Black;
/**
 * Represents the material properties of an object.
 */
public class Material {
    public BRDF brdf;
    public Pigment emittedRadiance;

    /**
     * Constructs a Material with default properties.
     */
    public Material(){
        this.brdf = new DiffuseBRDF();
        this.emittedRadiance = new UniformPigment(Black);
    }

    /**
     * Constructs a Material with the specified BRDF and default emitted radiance.
     *
     * @param brdf the BRDF of the material
     */
    public Material(BRDF brdf) {
        this.brdf = brdf;
        this.emittedRadiance = new UniformPigment(Black);
    }

    /**
     * Constructs a Material with the specified BRDF and emitted radiance.
     *
     * @param brdf             the BRDF of the material
     * @param emittedRadiance  the emitted radiance of the material
     */
    public Material(BRDF brdf, Pigment emittedRadiance) {
        this.brdf = brdf;
        this.emittedRadiance = emittedRadiance;
    }

}

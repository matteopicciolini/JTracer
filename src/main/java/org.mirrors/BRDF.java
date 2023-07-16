package org.mirrors;

import static org.mirrors.Global.Black;
import static org.mirrors.Global.White;

/**
 * The `BRDF` class represents a Bidirectional Reflectance Distribution Function,
 * which determines how light is reflected from a surface.
 *
 * This is an abstract class that provides a base implementation for evaluating the BRDF
 * and scattering rays based on the BRDF properties.
 *
 * The class contains a `pigment` field, which represents the color of the material.
 *
 * The `BRDF` class provides methods for evaluating the BRDF based on surface properties,
 * as well as scattering rays for recursive ray tracing.
 */
public abstract class BRDF {
    public Pigment pigment;

    /**
     * Constructs a `BRDF` object with a default uniform white pigment.
     */
    public BRDF(){
        this.pigment = new UniformPigment(White);
    }

    /**
     * Constructs a `BRDF` object with the given pigment.
     *
     * @param pigment the pigment representing the color of the material
     */
    public BRDF(Pigment pigment){
        this.pigment = pigment;
    }

    /**
     * Evaluates the BRDF for the given surface properties.
     * This method returns the reflected color for a given incoming direction, outgoing direction, and surface normal.
     *
     * @param norm    the surface normal
     * @param indDir  the incoming direction of light
     * @param outDir  the outgoing direction of reflected light
     * @param uv      the texture coordinates of the surface point
     * @return the color of the reflected light
     */
    public Color eval(Normal norm, Vec indDir, Vec outDir, Vec2d uv){
        return Black;
    };

    /**
     * Scatters a ray based on the BRDF properties for recursive ray tracing.
     * This method generates a new ray from the given interaction point, normal, and incoming direction.
     *
     * @param pcg              the random number generator
     * @param incomingDir      the incoming direction of the ray
     * @param interactionPoint the point of interaction with the surface
     * @param normal           the surface normal at the point of interaction
     * @param depth            the current recursion depth
     * @return the scattered ray
     */
    public abstract Ray scatterRay(PCG pcg, Vec incomingDir, Point interactionPoint, Normal normal, int depth);

}

package org.mirrors;

import static org.mirrors.Global.Black;

/**
 * Class representing a specular BRDF (Bidirectional Reflectance Distribution Function).
 */
public class SpecularBRDF extends BRDF{
    public float thresholdAngle = (float) (Math.PI/180.0f);

    /**
     * Constructor for the SpecularBRDF class.
     * Initializes the specular BRDF with the given pigment.
     *
     * @param pigment   the pigment used for the specular reflection
     */
    public SpecularBRDF(Pigment pigment){
            super(pigment);
    }

    /**
     * Default constructor for the SpecularBRDF class.
     * Initializes the specular BRDF with a black uniform pigment.
     */
    public SpecularBRDF(){
            super(new UniformPigment(Black));
    }


    /**
     * Evaluates the specular reflection for a given normal, incident direction, outgoing direction, and UV coordinates.
     *
     * @param normal    the surface normal
     * @param indDir    the incident direction
     * @param outDir    the outgoing direction
     * @param uv        the UV coordinates
     * @return          the color of the specular reflection
     */
    public Color eval(Normal normal, Vec indDir, Vec outDir, Vec2d uv){
        normal.normalize();
        indDir.normalize();
        outDir.normalize();
        float thetaIn = (float) Math.acos(normal.dot(indDir));
        float thetaOut = (float) Math.acos(normal.dot(outDir));


        if (Math.abs(thetaIn - thetaOut) < this.thresholdAngle)
            return this.pigment.getColor(uv);
        else
            return new Color(0.0f, 0.0f, 0.0f);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ray scatterRay(PCG pcg, Vec inDir, Point interPoint, Normal normal, int depth) {
        Vec rayDir = new Vec(inDir.x, inDir.y, inDir.z);
        rayDir.normalize();
        Vec normalVec = normal.toVec();
        normalVec.normalize();
        float dotProd = normalVec.dot(rayDir);

        return new Ray(
                interPoint,
                rayDir.minus((Vec) normalVec.dot( 2.f * dotProd)),
                depth);
    }
}
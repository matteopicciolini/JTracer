package org.mirrors;

import static org.mirrors.Global.Black;

public class SpecularBRDF extends BRDF{
    public float thresholdAngle = (float) (Math.PI/180.0f);
    public SpecularBRDF(Pigment pigment){
            super(pigment);
    }
    public SpecularBRDF(){
            super(new UniformPigment(Black));
    }


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

    @Override
    public Ray scatterRay(PCG pcg, Vec inDir, Point interPoint, Normal normal, int depth) {
        Vec rayDir = new Vec(inDir.x, inDir.y, inDir.z);
        rayDir.normalize();
        normal.normalize();
        float dotProd = normal.dot(rayDir);

        return new Ray(
                interPoint,
                rayDir.minus((Vec) normal.dot( 2.f * dotProd)),
                depth);
    }
}



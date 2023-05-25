package org.mirrors;

import static org.mirrors.Global.Black;

public class SpecularBRDF extends BRDF{

    float reflectance;
    public float thresholdAngle= (float) (Math.PI/180.0f);
    public SpecularBRDF(Pigment pigment, float reflectance){
            super(pigment);
            this.reflectance = reflectance;
    }
    public SpecularBRDF(){
            super(new UniformPigment(Black));
            this.reflectance = 1.f;
    }

        //ho scritto il metodo normalize che non ho finito e di conseguenza non funziona nulla
    public Color eval(Normal norm, Vec inDir,Vec outDir, Vec2d uv){
        float theta_in = (float) Math.acos(norm.dot(inDir).normalize());
        float theta_out = (float) Math.acos(norm.dot(outDir));

        if (Math.abs(theta_in - theta_out) < this.thresholdAngle)
            return this.pigment.getColor(uv);
        else
            return new Color(0.0f, 0.0f, 0.0f);
    }
    //manca la funxione to vec che però non so se serve davvero
    public Ray scatterRay(PCG pcg, Vec inDir, Point interPoint, Normal normal, int depth) {

        Vec ray_dir = new Vec(inDir.x, inDir.y, inDir.z);
        normal = normal.to_vec().normalize();
        float dot_prod = normal.dot(ray_dir);

        return Ray(
                interPoint,
                ray_dir.difference(normal.dot( 2f * dot_prod)),
                depth = depth);
    }
}



package org.mirrors;

import java.util.Objects;

import static java.lang.Math.max;
import static org.mirrors.Global.Black;

public class PathTracer extends Renderer{
    public PCG pcg;
    public int numOfRays;
    public int maxDepth;
    public int russianRouletteLimit;
    public PathTracer(World world) {
        super(world);
        this.pcg = new PCG();
        this.numOfRays = 10;
        this.maxDepth = 2;
        this.russianRouletteLimit = 3;
    }

    public PathTracer(World world, Color backgroundColor, PCG pcg, int numOfRays, int maxDepth, int russianRouletteLimit) {
        super(world, backgroundColor);
        this.pcg = pcg;
        this.numOfRays = numOfRays;
        this.maxDepth = maxDepth;
        this.russianRouletteLimit = russianRouletteLimit;
    }

    @Override
    public Color call(Ray ray) throws InvalidMatrixException {
        if (ray.depth > this.maxDepth){
            return Black;
        }
        HitRecord hitRecord = this.world.rayIntersection(ray);

        if(Objects.isNull(hitRecord)){
            return this.backgroundColor;
        }

        Material hitMaterial = hitRecord.shape.material;
        Color hitColor = hitMaterial.brdf.pigment.getColor(hitRecord.surfPoint);
        Color emittedRadiance = hitMaterial.emittedRadiance.getColor(hitRecord.surfPoint);
        float hitColorLum =  max(max(hitColor.r, hitColor.g), hitColor.b);

        //RUSSIAN ROULETTE
        if(ray.depth >= this.russianRouletteLimit){
            float q = (float) max(0.05, 1.f - hitColorLum);
            if (this.pcg.randomFloat() > q){
                hitColor = hitColor.prod(1.f /(1.f - q));
            }
            else{
                return emittedRadiance;
            }
        }

        //MonteCarlo Integration
        Color cumRadiance = Black;
        if(hitColorLum > 0.f){
            for(int rayIndex = 0; rayIndex < this.numOfRays; ++rayIndex){
                Ray newRay = hitMaterial.brdf.scatterRay(
                        this.pcg,
                        hitRecord.ray.dir,
                        hitRecord.worldPoint,
                        hitRecord.normal,
                        ray.depth + 1
                );
                // Recursive call
                Color newRadiance = this.call(newRay);
                cumRadiance = cumRadiance.sum(hitColor.prod(newRadiance));
            }
        }
        return emittedRadiance.sum(cumRadiance).prod((1.f / this.numOfRays));
    }
}

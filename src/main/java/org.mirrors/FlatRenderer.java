package org.mirrors;

import static org.mirrors.Global.Black;

public class FlatRenderer extends Renderer{

    public FlatRenderer(World world, Color backgroundColor){
        super(world, backgroundColor);
    }

    public FlatRenderer(World world){
        super(world, Black);
    }
    @Override
    public Color call(Ray ray) throws InvalidMatrixException {
        HitRecord hit = this.world.rayIntersection(ray);
        if(hit == null){
            return this.backgroundColor;
        }
        Material material = hit.shape.material;

        return (material.brdf.pigment.getColor(hit.surfPoint).sum(
                material.emittedRadiance.getColor(hit.surfPoint)));
    }
}

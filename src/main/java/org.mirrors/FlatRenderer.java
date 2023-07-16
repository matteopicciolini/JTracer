package org.mirrors;

import java.util.Objects;

import static org.mirrors.Global.Black;

/**
 * Represents a flat renderer that computes colors for rays in a world.
 */
public class FlatRenderer extends Renderer{

    /**
     * Constructs a flat renderer with the specified world and background color.
     *
     * @param world           the world to render
     * @param backgroundColor the background color
     */
    public FlatRenderer(World world, Color backgroundColor){
        super(world, backgroundColor);
    }

    /**
     * Constructs a flat renderer with the specified world and a default background color of black.
     *
     * @param world the world to render
     */
    public FlatRenderer(World world){
        super(world, Black);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color call(Ray ray) throws InvalidMatrixException {
        HitRecord hit = this.world.rayIntersection(ray);
        if(Objects.isNull(hit)){
            return this.backgroundColor;
        }
        Material material = hit.shape.material;

        return (material.brdf.pigment.getColor(hit.surfPoint).sum(
                material.emittedRadiance.getColor(hit.surfPoint)));
    }
}

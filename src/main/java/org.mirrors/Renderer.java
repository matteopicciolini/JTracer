package org.mirrors;

import static org.mirrors.Global.Black;

/**
 * Abstract class representing a renderer that converts rays to colors.
 */
public abstract class Renderer implements RayToColor {
    World world;
    Color backgroundColor;

    /**
     * Constructor for the Renderer class.
     *
     * @param world the world to render
     */
    public Renderer(World world) {
        this.world = world;
        this.backgroundColor = Black;
    }

    /**
     * Constructor for the Renderer class.
     *
     * @param world           the world to render
     * @param backgroundColor the background color
     */
    public Renderer(World world, Color backgroundColor) {
        this.world = world;
        this.backgroundColor = backgroundColor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract Color call(Ray ray) throws InvalidMatrixException;

}

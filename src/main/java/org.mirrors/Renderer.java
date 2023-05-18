package org.mirrors;

import static org.mirrors.Global.Black;

public abstract class Renderer implements RayToColor{
    World world;
    Color backgroundColor;

    public Renderer(World world) {
        this.world = world;
        this.backgroundColor = Black;
    }

    public Renderer(World world, Color backgroundColor) {
        this.world = world;
        this.backgroundColor = backgroundColor;
    }

    @Override
    public abstract Color call(Ray ray) throws InvalidMatrixException;

}

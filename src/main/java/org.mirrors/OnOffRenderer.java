package org.mirrors;

import java.util.Objects;

import static org.mirrors.Global.White;

public class OnOffRenderer extends Renderer{
    Color color;
    public OnOffRenderer(World world, Color backgroundColor, Color color) {
        super(world, backgroundColor);
        this.color = color;
    }
    public OnOffRenderer(World world) {
        super(world);
        this.color = White;
    }

    @Override
    public Color call(Ray ray) throws InvalidMatrixException {
        return Objects.isNull(this.world.rayIntersection(ray)) ? this.backgroundColor : this.color;
    }
}

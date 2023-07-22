package org.mirrors;

import java.util.Objects;

import static org.mirrors.Global.White;

/**
 * On-Off renderer class.
 */
public class OnOffRenderer extends Renderer {
    Color color;

    /**
     * Constructs an OnOffRenderer with the specified world, background color, and color.
     *
     * @param world           the world to render
     * @param backgroundColor the background color
     * @param color           the color to use when there is an intersection
     */
    public OnOffRenderer(World world, Color backgroundColor, Color color) {
        super(world, backgroundColor);
        this.color = color;
    }

    /**
     * Constructs an OnOffRenderer with the specified world and default background and color (white).
     *
     * @param world the world to render
     */
    public OnOffRenderer(World world) {
        super(world);
        this.color = White;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color call(Ray ray) throws InvalidMatrixException {
        return Objects.isNull(this.world.rayIntersection(ray)) ? this.backgroundColor : this.color;
    }
}

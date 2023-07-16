package org.mirrors;

/**
 * Abstract class representing a pigment.
 */
public abstract class Pigment {

    /**
     * Returns the color at the specified (u, v) coordinates.
     *
     * @param u the u-coordinate
     * @param v the v-coordinate
     * @return the color at the given coordinates
     */
    public abstract Color getColor(float u, float v);

    /**
     * Returns the color at the specified Vec2d coordinates.
     *
     * @param uv the Vec2d coordinates
     * @return the color at the given coordinates
     */
    public Color getColor(Vec2d uv){
        return getColor(uv.u, uv.v);
    }
}

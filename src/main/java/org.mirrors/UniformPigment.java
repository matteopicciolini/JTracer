package org.mirrors;

/**
 * Class representing a uniform pigment.
 */
public class UniformPigment extends Pigment {

    public Color color;

    /**
     * Constructor for the UniformPigment class.
     * Initializes the uniform pigment with the given color.
     *
     * @param color the color of the uniform pigment
     */
    public UniformPigment(Color color) {
        this.color = color;
    }

    /**
     * Default constructor for the UniformPigment class.
     * Initializes the uniform pigment with a black color.
     */
    public UniformPigment() {
        this.color = new Color();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getColor(float u, float v) {
        return this.color;
    }
}

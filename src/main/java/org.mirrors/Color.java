package org.mirrors;

import java.security.InvalidParameterException;

/**
 * Class used to set the color of a single pixel of an image.
 * Represents a color in the RGB color space with red, green, and blue components.
 */
public class Color {

    public float r;
    public float g;
    public float b;

    /**
     * Constructs a new Color object with all components set to 0.0.
     */
    public Color(){
        this.r = 0.0f;
        this.g = 0.0f;
        this.b = 0.0f;
    }

    /**
     * Constructs a new Color object with the specified red, green, and blue components.
     *
     * @param r The red component of the color.
     * @param g The green component of the color.
     * @param b The blue component of the color.
     * @throws InvalidParameterException if any of the components are negative.
     */
    public Color(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Adds the specified color to this color and returns the result.
     *
     * @param color1 The color to add to this color.
     * @return The sum of this color and the specified color.
     */
    public Color sum(Color color1){
        Color color = new Color();
        color.r = this.r + color1.r;
        color.g = this.g + color1.g;
        color.b = this.b + color1.b;
        return color;
    }

    /**
     * Multiplies this color by the specified scalar and returns the result.
     *
     * @param scalar The scalar to multiply this color by.
     * @return The product of this color and the specified scalar.
     */
    public Color prod(float scalar){
        Color color = new Color();
        color.r = this.r * scalar;
        color.g = this.g * scalar;
        color.b = this.b * scalar;
        return color;
    }

    /**
     * Multiplies this color by the specified color and returns the result.
     *
     * @param color1 The color to multiply this color by.
     * @return The product of this color and the specified color.
     */
    public Color prod(Color color1) {
        Color color = new Color();
        color.r = this.r * color1.r;
        color.g = this.g * color1.g;
        color.b = this.b * color1.b;
        return color;
    }

    /**
     * Calculates the absolute difference between this color and the specified color and returns the result.
     *
     * @param color1 The color to calculate the difference with.
     * @return The absolute difference between this color and the specified color.
     */
    public Color difference(Color color1){
        Color color = new Color();
        color.r = Math.abs(this.r - color1.r);
        color.g = Math.abs(this.g - color1.g);
        color.b = Math.abs(this.b - color1.b);
        return color;
    }

    /**
     * Checks if this color is close to the specified color within a small epsilon value and returns the result.
     *
     * @param color1 The color to compare to this color.
     * @return true if this color is close to the specified color, false otherwise.
     */
    public boolean isClose(Color color1){
        float epsilon = 1e-5F;
        Color color_diff = this.difference(color1);
        return (color_diff.r < epsilon &&
                color_diff.g < epsilon &&
                color_diff.b < epsilon);
    }

    /**
     * Returns a string representation of this color.
     *
     * @return A string representation of this color.
     */
    @Override
    public String toString() {
        return "<r: " + r + ", g: " + g + ", b: " + b + ">";
    }

    /**
     * Calculates the luminosity of this color using the formula (max(r, g, b) + min(r, g, b)) / 2.
     *
     * @return The luminosity of this color.
     */
    public float luminosity(){
        return ((Math.max(Math.max(this.r, this.g ), this.b)) + Math.min(Math.min(this.r, this.g), this.b))/2.0f;
    }
}
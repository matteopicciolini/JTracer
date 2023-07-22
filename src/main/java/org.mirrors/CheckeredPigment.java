package org.mirrors;

import static java.lang.Math.floor;

public class CheckeredPigment extends Pigment {

    public Color firstColor;
    public Color secondColor;
    public int stepsNumber;

    /**
     * Constructs a `CheckeredPigment` object with the given colors and steps number.
     *
     * @param firstColor  the first color for the checkered pattern
     * @param secondColor the second color for the checkered pattern
     * @param stepsNumber the number of steps in the checkered pattern
     */
    public CheckeredPigment(Color firstColor, Color secondColor, int stepsNumber) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.stepsNumber = stepsNumber;
    }

    /**
     * Constructs a `CheckeredPigment` object with the given colors and default steps number (10).
     *
     * @param firstColor  the first color for the checkered pattern
     * @param secondColor the second color for the checkered pattern
     */
    public CheckeredPigment(Color firstColor, Color secondColor) {
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.stepsNumber = 10;
    }

    /**
     * Returns the color of the checkered pattern at the given texture coordinates (u, v).
     * The checkered pattern is determined by the steps number and alternating first and second colors.
     *
     * @param u the horizontal texture coordinate
     * @param v the vertical texture coordinate
     * @return the color at the given texture coordinates
     */
    @Override
    public Color getColor(float u, float v) {
        int intU = (int) (floor(u * this.stepsNumber));
        int intV = (int) (floor(v * this.stepsNumber));
        return (intU % 2) == (intV % 2) ? this.firstColor : this.secondColor;
    }
}
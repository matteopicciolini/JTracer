package org.mirrors;

import static java.lang.Math.floor;

public class CheckeredPigment extends Pigment{

    Color firstColor;
    Color secondColor;
    int stepsNumber;

    CheckeredPigment(Color firstColor, Color secondColor, int stepsNumber){
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.stepsNumber = stepsNumber;
    }
    @Override
    public Color getColor(float u, float v) {
        int intU = (int)(floor(u * this.stepsNumber));
        int intV = (int)(floor(v * this.stepsNumber));
        return (intU % 2) == (intV % 2) ? this.firstColor : this.secondColor;
    }
}
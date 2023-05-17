package org.mirrors;

public class UniformPigment extends Pigment{

    Color color;
    public UniformPigment(Color color){
        this.color = color;
    }

    @Override
    public Color getColor(float u, float v) {
        return this.color;
    }
}

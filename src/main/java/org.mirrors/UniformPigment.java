package org.mirrors;

public class UniformPigment extends Pigment{

    public Color color;
    public UniformPigment(Color color){
        this.color = color;
    }
    public UniformPigment(){
        this.color = new Color();
    }
    @Override
    public Color getColor(float u, float v) {
        return this.color;
    }
}

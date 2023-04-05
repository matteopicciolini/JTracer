package org.mirrors;

public class Normal extends OrientedObject {
    public Normal() {
        super();
    }
    public Normal(float x, float y, float z) {
        super(x, y, z);
    }

    @Override
    protected Normal createInstance(float x, float y, float z) {
        return new Normal(x, y, z);
    }

    public Normal cross(Vec other){
        return cross(this, other, Normal.class);
    }

    public Normal cross(Normal other){
        return cross(this, other, Normal.class);
    }
}
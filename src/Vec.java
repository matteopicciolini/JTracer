import AbstractClasses.OrientedObject;

public class Vec extends OrientedObject {
    public Vec() {
        super();
    }
    public Vec(float x, float y, float z) {
        super(x, y, z);
    }

    @Override
    public Vec createInstance(float x, float y, float z) {return new Vec(x, y, z);}
    protected Vec minus(Vec other){return difference(this, other, Vec.class);}
    public Vec sum(Vec other){
        return sum(this, other, Vec.class);
    }
    public Vec cross(Vec other){
        return cross(this, other, Vec.class);
    }
}
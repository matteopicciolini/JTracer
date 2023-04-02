import AbstractClasses.Geometry;

public class Point extends Geometry {
    protected Vec toVec(){
        return new Vec(this.x , this.y, this.z);
    }
    public Point(){
        super();
    }
    public Point(float x, float y, float z){
        super(x, y, z);
    }

    @Override
    protected Point createInstance(float x, float y, float z) {
        return new Point(x, y, z);
    }
    public Vec minus(Point other){
        return difference(this, other, Vec.class);
    }
    public Point minus(Vec other){
        return difference(this, other, Point.class);
    }
    public Point sum(Vec other){
        return sum(this, other, Point.class);
    }
}
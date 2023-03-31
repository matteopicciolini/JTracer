import AbstractClasses.OrientedObject;

public class Vec extends OrientedObject {
    public Vec() {
        super();
    }
    public Vec(float x, float y, float z) {
        super(x, y, z);
    }

    @Override
    public Vec createInstance(float x, float y, float z) {
        return new Vec(x, y, z);
    }


    protected Vec difference(Vec other){
        return difference(this, other, Vec.class);
    }

    public Vec sum(Vec other){
        return sum(this, other, Vec.class);
    }

    public Vec cross(Vec other){
        return cross(this, other, Vec.class);
    }
    /*
    public boolean is_close(Vec vec1){
        float epsilon = 1e-5F;
        Vec vec_diff = this.difference(vec1);
        return (vec_diff.x < epsilon &&
                vec_diff.y < epsilon &&
                vec_diff.z < epsilon);
    }


    public Vec sum(Vec vector){
        return AbstractClasses.Geometry.sum(this, vector, Vec::new);
    }

    public float dot(Vec vector){
        return AbstractClasses.Geometry.dot(this, vector);
    }
*/
}
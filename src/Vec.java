public class Vec implements Geometry{
    public float x;
    public float y;
    public float z;

    @Override
    public String toString() {return "Vec(%s, %s, %s)".formatted(x, y, z);}

    public float get_x(){return this.x;};
    public float get_y(){return this.y;};
    public float get_z(){return this.z;};

    public void set_x(float x){this.x = x;};
    public void set_y(float y){this.y = y;};
    public void set_z(float z){this.z = z;};

    public Vec(){
        this.x=0;
        this.y=0;
        this.z=0;
    }

    public Vec(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public boolean is_close(Vec vec1){
        float epsilon = 1e-5F;
        Vec vec_diff = this.difference(vec1);
        return (vec_diff.x < epsilon &&
                vec_diff.y < epsilon &&
                vec_diff.z < epsilon);
    }

    public Vec difference(Vec vector){
        return Geometry.difference(this, vector, Vec::new);
    }
    public Vec sum(Vec vector){
        return Geometry.sum(this, vector, Vec::new);
    }

}
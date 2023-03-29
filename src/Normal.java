public class Normal {

    public float x;
    public float y;
    public float z;

    public Normal() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Normal(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }


    @Override
    public String toString() {
        return "<r: %s, g: %s, b: %s>".formatted(this.x, this.y, this.z);
    }

    public Normal difference(Normal normal1) {
        Normal normal = new Normal(Math.abs(this.x - normal1.x), Math.abs(this.y - normal1.y), Math.abs(this.z - normal1.z));
        return normal;
    }

    public boolean is_close(Normal normal1) {
        float epsilon = 1e-5F;
        Normal norm_diff = this.difference(normal1);
        return (norm_diff.x < epsilon &&
                norm_diff.y < epsilon &&
                norm_diff.z < epsilon);
    }

    public Normal negation() {
        Normal normal = new Normal(-this.x, -this.y, -this.z);
        return normal;
    }

    public Normal dilatation(float scal) {
        Normal normal = new Normal(this.x * scal, this.y * scal, this.z * scal);
        return normal;
    }
    public float dot(Normal normal1) {

        return x*normal1.x+y*normal1.y+z*normal1.z;
    }

    public float norm() {

        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public float norm_sq(Normal normal1) {
        return x * x + y * y + z * z;
    }

    public Normal normalization() {
        Normal norm=new Normal(x, y, z);
        return norm.dilatation(1/norm.norm());
    }
    public Normal cross(Normal normal1) {
                Normal normal2= new Normal(y * normal1.z - z * normal1.y, z * normal1.x - x * normal1.z, x* normal1.y - y * normal1.x);
                return normal2;
    }
    public Normal cross(Vec vec) {
        return cross(conversion(vec));

    }
    public Normal conversion(Vec vec){
        Normal norm=new Normal(x= vec.x, y= vec.y, z= vec.z);
        return norm;

    }

}
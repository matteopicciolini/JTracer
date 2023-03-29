public class Point {
    public float x;
    public float y;
    public float z;

    public Point(){
        this.x=0;
        this.y=0;
        this.z=0;
    }
    public Point(float x, float y, float z){
        this.x=x;
        this.y=y;
        this.z=z;
    }


    @Override
    public String toString() {
        return "<r: %s, g: %s, b: %s>".formatted(this.x, this.y, this.z);
    }
    public Point difference(Point point1){
        Point point = new Point();
        point.x = Math.abs(this.x - point1.x);
        point.y = Math.abs(this.y - point1.y);
        point.z = Math.abs(this.z - point1.z);
        return point;
    }
    public boolean is_close(Point point1){
        float epsilon = 1e-5F;
        Point point_diff = this.difference(point1);
        return (point_diff.x < epsilon &&
                point_diff.y < epsilon &&
                point_diff.z < epsilon);
    }
    public Point sum_point_vec(Vec vec){
        Point point = new Point();
        point.x = Math.abs(this.x + vec.x);
        point.y = Math.abs(this.y + vec.y);
        point.z = Math.abs(this.z + vec.z);
        return point;
    }

    public Vec diff_point_point(Point p1){
        Vec vec = new Vec();
        vec.x = Math.abs(this.x - p1.x);
        vec.y = Math.abs(this.y - p1.y);
        vec.z = Math.abs(this.z - p1.z);
        return vec;
    }
    public Point diff_point_vec(Vec v1){
        Point point = new Point(Math.abs(this.x - v1.x), Math.abs(this.y - v1.y), Math.abs(this.z - v1.z));
        return point;
    }
    public Vec to_vec(){
        Vec vec = new Vec((this.x), (this.y), (this.z));
        return vec;
    }
}
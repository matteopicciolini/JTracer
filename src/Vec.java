
    public class Vec {
        public float x;
        public float y;
        public float z;

        public Vec(){
            this.x=0;
            this.y=0;
            this.z=0;
        }
        public Vec(float x, float y, float z){
            this.x=x;
            this.y=y;
            this.z=z;
        }


        @Override
        public String toString() {
            return "<r: %s, g: %s, b: %s>".formatted(this.x, this.y, this.z);
        }
        public Vec difference(Vec Vec1){
            Vec vec = new Vec();
            vec.x = Math.abs(this.x - Vec1.x);
            vec.y = Math.abs(this.y - Vec1.y);
            vec.z = Math.abs(this.z - Vec1.z);
            return vec;
        }
        public boolean is_close(Vec vec1){
            float epsilon = 1e-5F;
            Vec vec_diff = this.difference(vec1);
            return (vec_diff.x < epsilon &&
                    vec_diff.y < epsilon &&
                    vec_diff.z < epsilon);
        }
    }


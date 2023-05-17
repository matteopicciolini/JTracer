package org.mirrors;

public class PCG {


    public class PGC {
        public int state=0;
        public int inc =0;
        public PGC(int init_state, int init_seq){
            this.inc=(init_seq << 1) | 1;
            random();
            this.state= this.state + init_state;
            random();

        }
        public long random(){
            long old_state=this.state;
            this.state = ((old_state * 6364136223846793005) + this.inc);
            return 0;
        }
    }

}

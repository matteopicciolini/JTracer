package org.mirrors;

    public class PCG {
        public long state;
        public long inc;

        public PCG(int init_state, int init_seq) {
            this.state = 0;
            this.inc = (init_seq << 1) | 1;
            this.random();
            this.state += init_state;
            this.random();

        }

        public long random() {
            long old_state = this.state;
            this.state = ((old_state * 6364136223846793005l) + this.inc);
            int xorshifted = (int) (((old_state >>> 18) ^ old_state) >>> 27);
            int rot = (int) (old_state >>> 59);
            return Integer.rotateRight(xorshifted, rot) & 0xffffffffL;
        }
    }


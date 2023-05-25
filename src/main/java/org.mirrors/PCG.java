package org.mirrors;

public class PCG {
    public long state;
    public long inc;

    public PCG(){
        this.state = 0;
        this.inc = (54 << 1) | 1;
        this.random();
        this.state += 42;
        this.random();
    }

    public PCG(int initState, int initSeq) {
        this.state = 0;
        this.inc = ((long) initSeq << 1) | 1;
        this.random();
        this.state += initState;
        this.random();
    }

    public long random() {
        long oldState = this.state;
        this.state = ((oldState * 6364136223846793005L) + this.inc);
        int xorShifted = (int) (((oldState >>> 18) ^ oldState) >>> 27);

        int rot = (int) (oldState >>> 59);
        return Integer.rotateRight(xorShifted, rot) & 0xffffffffL;
    }

    public float randomFloat() {
        return this.random() / 0xffffffff;
    }
}


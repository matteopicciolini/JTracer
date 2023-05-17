package org.mirrors;

public class PCG {
    public long state;
    public long inc;

    public PCG(int initState, int initSeq){
        this.state = 0;
        this.inc = (initSeq << 1) | 1;
        this.random();
        this.state += initState;
        this.random();
    }
    public long random(){
        long oldState = this.state;
        this.state = ((oldState * 6364136223846793005l) + this.inc);
        int xorShifted = (int)(((oldState >>> 18) ^ oldState) >>> 27);

        int rot = (int)(oldState >>> 59);
        return Integer.rotateRight(xorShifted, rot) & 0xffffffffL;
    }

    public float random_float(){
        return this.random() / 0xffffffff;
    }
}

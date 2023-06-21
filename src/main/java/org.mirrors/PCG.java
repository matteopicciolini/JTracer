package org.mirrors;

/**

 This class represents a PCG (Permuted Congruential Generator) used for generating pseudo-random numbers.
 It implements a 64-bit PCG algorithm.
 */
public class PCG {
    public long state;
    public long inc;

    /**
     * Constructs a PCG object with default initial state and sequence values.
     * The generator is seeded with two initial random numbers.
     */
    public PCG(){
        this.state = 0;
        this.inc = (54 << 1) | 1;
        this.random();
        this.state += 42;
        this.random();
    }

    /**
     * Constructs a PCG object with a given initial state and sequence values.
     * The generator is seeded with two initial random numbers.
     *
     * @param initState the initial state value
     * @param initSeq   the initial sequence value
     */
    public PCG(int initState, int initSeq) {
        this.state = 0;
        this.inc = ((long) initSeq << 1) | 1;
        this.random();
        this.state += initState;
        this.random();
    }

    /**
     * Generates the next random number in the sequence.
     *
     * @return the next random number
     */
    public long random() {
        long oldState = this.state;
        this.state = ((oldState * 6364136223846793005L) + this.inc);
        int xorShifted = (int) (((oldState >>> 18) ^ oldState) >>> 27);

        int rot = (int) (oldState >>> 59);
        return Integer.rotateRight(xorShifted, rot) & 0xffffffffL;
    }

    /**
     * Generates the next random floating-point number between 0 (inclusive) and 1 (exclusive).
     *
     * @return the next random floating-point number
     */
    public float randomFloat() {
        //return this.random() / 0xffffffff;
        return (float) (this.random() / (double) 0xffffffffL);
    }
}

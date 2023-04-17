package org.mirrors;

/**
 * The Vec class represents a vector in three-dimensional space. It provides x, y, and z components for the vector,
 * which can be accessed or modified. This class also provides methods for
 * vector arithmetic, such as adding and subtracting vectors, scaling vectors, and calculating the dot product and cross
 * product of two vectors.
 */
public class Vec extends OrientedObject {
    /**
     * Constructor for the Vec class with no arguments.
     * Initializes x, y, and z to 0.
     */
    public Vec() {
        super();
    }

    /**
     * Constructor for the Vec class with three arguments.
     * Initializes x, y, and z to the corresponding arguments passed.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
    public Vec(float x, float y, float z) {
        super(x, y, z);
    }

    /**
     * Creates a new instance of Vec with the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     * @return A new instance of Vec with the specified coordinates.
     */
    @Override
    public Vec createInstance(float x, float y, float z) {return new Vec(x, y, z);}

    /**
     * Returns a new Vec object that represents the difference between the current Vec and the input Vec.
     *
     * @param other The Vec to subtract from the current Vec.
     * @return A new Vec object that represents the difference between the current Vec and the input Vec.
     */
    protected Vec minus(Vec other){return difference(this, other, Vec.class);}

    /**
     * Returns a new Vec object that represents the sum of the current Vec and the input Vec.
     *
     * @param other The Vec to add to the current Vec.
     * @return A new Vec object that represents the sum of the current Vec and the input Vec.
     */
    public Vec sum(Vec other){
        return sum(this, other, Vec.class);
    }

    /**
     * Returns a new Vec object that represents the cross product of the current Vec and the input Vec.
     *
     * @param other The Vec to calculate the cross product with.
     * @return A new Vec object that represents the cross product of the current Vec and the input Vec.
     */
    public Vec cross(Vec other){
        return cross(this, other, Vec.class);
    }
}
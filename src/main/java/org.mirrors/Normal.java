package org.mirrors;

/**
 * The Normal class represents a normal vector in three-dimensional space. A normal vector is a vector that is
 * perpendicular to a surface. This class provides x, y, and z components for the normal vector, which can be accessed.
 * This class also provides methods for vector arithmetic,
 * such as adding and subtracting normal vectors, scaling normal vectors, and calculating the dot product and cross
 * product of two normal vectors.
 */
public class Normal extends OrientedObject {

    /**
     * Constructor for the Normal class with no arguments.
     * Initializes x, y, and z to 0.
     */
    public Normal() {
        super();
    }

    /**
     * Constructor for the Normal class with three arguments.
     * Initializes x, y, and z to the corresponding arguments passed.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
    public Normal(float x, float y, float z) {
        super(x, y, z);
    }

    /**
     * Creates a new instance of Normal with the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     * @return A new instance of Normal with the specified coordinates.
     */
    @Override
    protected Normal createInstance(float x, float y, float z) {
        return new Normal(x, y, z);
    }

    /**
     * Returns a new Normal object that represents the cross product of the current Normal and the input Vec.
     *
     * @param other The Vec to calculate the cross product with.
     * @return A new Normal object that represents the cross product of the current Normal and the input Vec.
     */
    public Normal cross(Vec other) {
        return cross(this, other, Normal.class);
    }

    /**
     * Returns a new Normal object that represents the cross product of the current Normal and the input Normal.
     *
     * @param other The Normal to calculate the cross product with.
     * @return A new Normal object that represents the cross product of the current Normal and the input Normal.
     */
    public Normal cross(Normal other) {
        return cross(this, other, Normal.class);
    }

    public Vec toVec() {
        return new Vec(this.x, this.y, this.z);
    }
}
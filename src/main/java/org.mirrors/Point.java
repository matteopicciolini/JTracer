package org.mirrors;

/**
 * The Point class represents a point in three-dimensional space. This class extends Geometry and inherits its x, y, and z
 * coordinates. It also provides methods for converting a Point to a Vec, subtracting Points and Vecs, and adding Vecs to Points.
 */
public class Point extends Geometry {

    /**
     * Constructor for the Point class with no arguments.
     * Initializes x, y, and z to 0.
     */
    public Point() {
        super();
    }

    /**
     * Constructor for the Point class with three arguments.
     * Initializes x, y, and z to the corresponding arguments passed.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
    public Point(float x, float y, float z) {
        super(x, y, z);
    }

    /**
     * Converts a Point object to a Vec object with the same coordinates.
     *
     * @return A new Vec object with the same coordinates as the current Point.
     */
    protected Vec toVec() {
        return new Vec(this.x, this.y, this.z);
    }

    /**
     * Creates a new instance of Point with the specified coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     * @return A new instance of Point with the specified coordinates.
     */
    @Override
    protected Point createInstance(float x, float y, float z) {
        return new Point(x, y, z);
    }

    /**
     * Returns a new Vec object that represents the difference between the current Point and the input Point.
     *
     * @param other The Point to subtract from the current Point.
     * @return A new Vec object that represents the difference between the current Point and the input Point.
     */
    public Vec minus(Point other) {
        return difference(this, other, Vec.class);
    }

    /**
     * Returns a new Point object that represents the difference between the current Point and the input Vec.
     *
     * @param other The Vec to subtract from the current Point.
     * @return A new Point object that represents the difference between the current Point and the input Vec.
     */
    public Point minus(Vec other) {
        return difference(this, other, Point.class);
    }

    /**
     * Returns a new Point object that represents the sum of the current Point and the input Vec.
     *
     * @param other The Vec to add to the current Point.
     * @return A new Point object that represents the sum of the current Point and the input Vec.
     */
    public Point sum(Vec other) {
        return sum(this, other, Point.class);
    }
}
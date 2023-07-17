package org.mirrors;
import java.lang.reflect.InvocationTargetException;
import static java.lang.Math.abs;

/**
 * The Geometry class represents an abstract geometric object in three-dimensional space. It provides x, y, and z coordinates
 * for the object's position in space, which can be accessed or modified.
 * This class also provides methods for checking if two geometries intersect and for calculating the distance between
 * two geometries.
 */

public abstract class Geometry{
    public float x;
    public float y;
    public float z;

    /**
     * Constructor for the Geometry class with no arguments.
     * Initializes x, y, and z to 0.
     */
    public Geometry(){
        this.x = 0.f;
        this.y = 0.f;
        this.z = 0.f;
    }

    /**
     * Constructor for the Geometry class with three arguments.
     * Initializes x, y, and z to the corresponding arguments passed.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
    public Geometry(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Abstract method that subclasses of Geometry must implement.
     * Returns a new instance of the subclass with the given coordinates.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     * @return A new instance of the subclass with the given coordinates.
     */
    protected abstract Geometry createInstance(float x, float y, float z);

    /**
     * Returns a new instance of the same subclass with each coordinate multiplied by the given scalar value.
     *
     * @param lambda The scalar value to multiply each coordinate by.
     * @return A new instance of the same subclass with each coordinate multiplied by the given scalar value.
     */
    public Geometry dot(float lambda) {
        return (createInstance(this.x * lambda, this.y * lambda, this.z * lambda));
    }

    /**
     * Returns a new instance of the specified subclass with coordinates equal to the difference between the coordinates of the two input Geometry objects.
     *
     * @param a The first Geometry object.
     * @param b The second Geometry object.
     * @param returnType The class of the subclass to return.
     * @return A new instance of the specified subclass with coordinates equal to the difference between the coordinates of the two input Geometry objects.
     */
    protected static <T extends Geometry> T difference(Geometry a, Geometry b, Class<T> returnType) {
        try {
            T result = returnType.getDeclaredConstructor().newInstance();
            result.x = a.x - b.x;
            result.y = a.y - b.y;
            result.z = a.z - b.z;
            return result;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    protected static <T extends Geometry> T sum(Geometry a, float b, Class<T> returnType) {
        try {
            T result = returnType.getDeclaredConstructor().newInstance();
            result.x = a.x + b;
            result.y = a.y + b;
            result.z = a.z + b;
            return result;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a new instance of the specified subclass with coordinates equal to the sum of the coordinates of the two input Geometry objects.
     *
     * @param a The first Geometry object.
     * @param b The second Geometry object.
     * @param returnType The class of the subclass to return.
     * @return A new instance of the specified subclass with coordinates equal to the sum of the coordinates of the two input Geometry objects.
     */
    protected static <T extends Geometry> T sum(Geometry a, Geometry b, Class<T> returnType) {
        try {
            T result = returnType.getDeclaredConstructor().newInstance();
            result.x = a.x + b.x;
            result.y = a.y + b.y;
            result.z = a.z + b.z;
            return result;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns true if the coordinates of the input Geometry object are within a certain epsilon value of the coordinates of the current Geometry object, and false otherwise.
     *
     * @param other The Geometry object to compare to the current object.
     * @return True if the coordinates of the input Geometry object are within a certain epsilon value of the coordinates of the current Geometry object, and false otherwise.
     * @throws IllegalArgumentException If the input Geometry object is of a different class than the current object.
     */
    public boolean isClose(Geometry other) {
        //if (other.getClass() != this.getClass()){throw new IllegalArgumentException("Cannot compare different types");}
        float epsilon = 1e-5F;
        Geometry diff = difference(this, other, this.getClass());
        return (abs(diff.x) < epsilon &&
                abs(diff.y) < epsilon &&
                abs(diff.z) < epsilon);
    }
    /**
     * Returns a string representation of the current Geometry object in the format ClassName(x, y, z), where ClassName is the name of the actual subclass of Geometry and x, y, and z are the coordinates of the Geometry object.
     *
     * @return A string representation of the current Geometry object.
     */
    @Override
    public String toString() {return "%s(%s, %s, %s)".formatted(this.getClass().getName(), x, y, z);}

    public float get(int i) {
        switch (i) {
            case 0:
                return x;
            case 1:
                return y;
            case 2:
                return z;
            default:
                throw new IndexOutOfBoundsException();
        }
    }
}
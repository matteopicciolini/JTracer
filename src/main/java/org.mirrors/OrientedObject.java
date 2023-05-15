package org.mirrors;
import java.lang.reflect.InvocationTargetException;
import static java.lang.Math.sqrt;

/**
 * The OrientedObject class represents an object in three-dimensional space with a position and orientation. It extends
 * the Geometry class and inherits its x, y, and z coordinates. This class also provides methods for setting and getting
 * the object's orientation using Euler angles.
 */
public abstract class OrientedObject extends Geometry {
    /**
     * Constructor for the OrientedObject class with no arguments.
     * Initializes x, y, and z to 0.
     */
    public OrientedObject() {
        super();
    }

    /**
     * Constructor for the OrientedObject class with three arguments.
     * Initializes x, y, and z to the corresponding arguments passed.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param z The z-coordinate.
     */
    public OrientedObject(float x, float y, float z) {
        super(x, y, z);
    }

    /**
     * Returns a new instance of the current OrientedObject with each coordinate negated.
     *
     * @return A new instance of the current OrientedObject with each coordinate negated.
     */
    public Geometry neg() {
        return (createInstance(-this.x, -this.y, -this.z));
    }

    /**
     * Returns the dot product of the current OrientedObject and the input OrientedObject.
     *
     * @param other The OrientedObject to calculate the dot product with.
     * @return The dot product of the current OrientedObject and the input OrientedObject.
     */
    public float dot(OrientedObject other) {
        return (this.x * other.x +
                this.y * other.y +
                this.z * other.z
        );
    }

    /**
     * Returns the Euclidean norm (magnitude) of the current OrientedObject.
     *
     * @return The Euclidean norm (magnitude) of the current OrientedObject.
     */
    public float norm(){
        return (float) sqrt(this.squaredNorm());
    }

    /**
     * Returns the squared Euclidean norm of the current OrientedObject.
     *
     * @return The squared Euclidean norm of the current OrientedObject.
     */
    public float squaredNorm(){
        return this.dot(this);
    }

    /**
     * Normalizes the current OrientedObject to have unit length.
     */
    public void normalize(){
        float norm = this.norm();
        this.x /= norm;
        this.y /= norm;
        this.z /= norm;
    }

    /**
     * Returns the cross product of two OrientedObjects as a new instance of the specified class type.
     *
     * @param a The first OrientedObject.
     * @param b The second OrientedObject.
     * @param returnType The class type of the returned instance.
     * @param <T> The type of the returned instance.
     * @return The cross product of the two OrientedObjects as a new instance of the specified class type.
     */
    protected static <T extends OrientedObject> T cross(OrientedObject a, OrientedObject b, Class<T> returnType) {
        try {
            T result = returnType.getDeclaredConstructor().newInstance();
            result.x = a.y * b.z - a.z * b.y;
            result.y = a.z * b.x - a.x * b.z;
            result.z = a.x * b.y - a.y * b.x;
            return result;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
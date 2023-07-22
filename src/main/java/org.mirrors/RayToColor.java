package org.mirrors;

/**
 * The RayToColor interface represents a function that maps a given Ray object to a Color object.
 */
public interface RayToColor {
    /**
     * Maps the given Ray object to a Color object.
     *
     * @param ray The Ray object to be mapped.
     * @return The Color object that the Ray object is mapped to.
     */
    Color call(Ray ray) throws InvalidMatrixException;
}

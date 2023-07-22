package org.mirrors;

/**
 * Abstract base class for Constructive Solid Geometry (CSG) operations.
 */
public abstract class CSG extends Shape {
    public Shape shape1;
    public Shape shape2;

    /**
     * Constructs a CSG operation with the specified transformation, shape1, and shape2.
     *
     * @param transformation the transformation matrix for the CSG operation
     * @param shape1         the first shape in the CSG operation
     * @param shape2         the second shape in the CSG operation
     */
    public CSG(Transformation transformation, Shape shape1, Shape shape2) {
        super(transformation);
        this.shape1 = shape1;
        this.shape2 = shape2;
    }

    /**
     * Constructs a CSG operation with the specified shape1 and shape2.
     *
     * @param shape1 the first shape in the CSG operation
     * @param shape2 the second shape in the CSG operation
     */
    public CSG(Shape shape1, Shape shape2) {
        super();
        this.shape1 = shape1;
        this.shape2 = shape2;
    }
}

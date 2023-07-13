package org.mirrors;

import java.util.List;

public abstract class CSG extends Shape{
    public Shape shape1;
    public Shape shape2;

    public CSG(Transformation transformation, Shape shape1, Shape shape2){
        super(transformation);
        this.shape1 = shape1;
        this.shape2 = shape2;
    }

    public CSG(Shape shape1, Shape shape2){
        super();
        this.shape1 = shape1;
        this.shape2 = shape2;
    }
}

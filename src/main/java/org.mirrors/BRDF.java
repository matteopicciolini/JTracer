package org.mirrors;

public abstract class BRDF {
    public Pigment pig;
    public float reflectance;
    public abstract Color eval(Normal norm, Vec ind_dir, Vec2d uv);
}

package org.mirrors;

public abstract class BRDF {
    public Normal norm;
    public Vec ind_dir;
    public Vec2d uv;
    public Vec out_dir;

    public abstract Color eval(Normal norm, Vec ind_dir, Vec2d uv);
}

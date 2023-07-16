package org.mirrors;

/**
 * Orthonormal basis (ONB) class.
 */
public class ONB {
    Vec e1, e2, e3;

    /**
     * Constructs an orthonormal basis from a given normal vector.
     *
     * @param normal the normal vector
     */
    public ONB(Normal normal) {
        //float sign = Math.signum(normal.z);
        normal.normalize();
        float sign = normal.z > 0.f ? 1.f : -1.f;
        float a = -1.f / (sign + normal.z);
        float b = normal.x * normal.y * a;

        this.e1 = new Vec(1.f + sign * normal.x * normal.x * a, sign * b, - sign * normal.x);
        this.e2 = new Vec(b, sign + normal.y * normal.y * a, -normal.y);
        this.e3 = new Vec(normal.x, normal.y, normal.z);
    }

}

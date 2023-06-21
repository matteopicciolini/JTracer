package org.mirrors;

import java.awt.Color;

public class Triangle extends Shape{

    public Triangle() {
        super();
    }
    public Triangle(Transformation transformation) {
        super(transformation);
    }
    public Triangle(Material material) {
        super(material);
    }
    public Triangle(Transformation transformation, Material material) {
        super(transformation, material);
    }
    private Vec v0;  // Vertice 0
    private Vec v1;  // Vertice 1
    private Vec v2;  // Vertice 2
    private Color color;  // Colore del triangolo

    public Triangle(Vec v0, Vec v1, Vec v2, Color color) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        this.color = color;
    }

    public HitRecord rayIntersection(Ray ray) throws InvalidMatrixException {

// compute the plane's normal

        Normal N = calculateNormal(); // N
        float area = calculateTriangleArea(v0, v1, v2);

        // Step 1: finding P

        // check if the ray and plane are parallel.

        if (Math.abs(N.dot(ray.dir)) < 1e-5) // almost 0
            return false; // they are parallel, so they don't intersect!

        // compute d parameter using equation 2
        float d = -N.dotProduct(v0);

        // compute t (equation 3)
        t = -(N.dotProduct(orig) + d) / NdotRayDirection;

        // check if the triangle is behind the ray
        if (t < 0) return false; // the triangle is behind

        // compute the intersection point using equation 1
        Vec3f P = orig + t * dir;

        // Step 2: inside-outside test
        Vec3f C; // vector perpendicular to triangle's plane

        // edge 0
        Vec3f edge0 = v1 - v0;
        Vec3f vp0 = P - v0;
        C = edge0.crossProduct(vp0);
        if (N.dotProduct(C) < 0) return false; // P is on the right side

        // edge 1
        Vec3f edge1 = v2 - v1;
        Vec3f vp1 = P - v1;
        C = edge1.crossProduct(vp1);
        if (N.dotProduct(C) < 0)  return false; // P is on the right side

        // edge 2
        Vec3f edge2 = v0 - v2;
        Vec3f vp2 = P - v2;
        C = edge2.crossProduct(vp2);
        if (N.dotProduct(C) < 0) return false; // P is on the right side;

        return true; // this ray hits the triangle
    }
        private Normal calculateNormal() {
            // Calcola il vettore normale al piano del triangolo utilizzando il prodotto vettoriale tra due lati
            Vec side1 = v1.minus(v0);
            Vec side2 = v2.minus(v0);
            Normal norm = (side1.cross(side2)).toNormal();
            norm.normalize();
            return norm;
        }

        private float calculateTriangleArea(Vec p0, Vec p1, Vec p2) {
            // Calcola l'area del triangolo utilizzando il prodotto vettoriale tra due lati
            Vec side1 = p1.minus(p0);
            Vec side2 = p2.minus(p0);
            return side1.dot(side2);
    }


    public Color getColor() {
        return color;
    }
}


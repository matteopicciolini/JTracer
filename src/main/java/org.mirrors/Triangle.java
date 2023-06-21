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

        if (Math.abs(N.dot(ray.dir)) < 1e-5)
            return false;

        // compute d parameter using equation 2
        float d = -N.dot(v0);

        // compute t (equation 3)
        float t = -(N.dot(ray.origin) + d) / N.dot(ray.dir);

        // check if the triangle is behind the ray
        if (t < 0)
            return false;

        // compute the intersection point using equation 1
        Vec P = ray.origin.sum(ray.dir.dot(t));

        // Step 2: inside-outside test
        Vec C; // vector perpendicular to triangle's plane

        // edge 0
        Vec edge0 = v1.minus(v0);
        Vec vp0 = P.minus(v0);
        C = edge0.cross(vp0);
        if (N.dot(C) < 0)
            return false; // P is on the right side

        // edge 1
        Vec edge1 = v2.minus(v1);
        Vec vp1 = P.minus(v1);
        C = edge1.cross(vp1);
        if (N.dot(C) < 0)
            return false; // P is on the right side

        // edge 2
        Vec edge2 = v0.minus(v2);
        Vec vp2 = P.minus(v2);
        C = edge2.cross(vp2);
        if (N.dot(C) < 0)
            return false; // P is on the right side;

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


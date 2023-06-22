package org.mirrors;

import java.util.ArrayList;
import java.util.List;

public class Triangle extends Shape{
    public Triangle(Transformation transformation) {
        super(transformation);
    }
    public Triangle(Material material) {
        super(material);
    }
    public Triangle(Transformation transformation, Material material) {
        super(transformation, material);
    }

    public Vec v0;  // Vertice 0
    public Vec v1;  // Vertice 1
    public Vec v2;  // Vertice 2
    public Vec e1;  // lato 1
    public Vec e2;  // lato 2
    public Normal norm;

    public Triangle(Vec v0, Vec v1, Vec v2, Material material) {
        super(material);
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
    }
    public Triangle(Vec v0, Vec v1, Vec v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
    }

    public HitRecord rayIntersection(Ray ray) throws InvalidMatrixException {
        // Applica la trasformazione inversa al raggio
        Ray iray = transformation.inverse().times(ray);
        Normal norm= calculateNormal();

        // Prodotto vettoriale tra direzione del raggio e il secondo lato del triangolo
        Vec pvec = iray.dir.cross(e2);

        // Calcola determinante tra il lato del triangolo e pvec
        float det = e1.dot(pvec, Vec.class);

        // Condizione di parallelismo
        if (Math.abs(det) < 1e-5) {return null;}

        float invDet = 1.0f / det;

        // Calcola il vettore dal punto di origine del raggio al primo vertice del triangolo
        Vec tvec = iray.origin.minus(v0).toVec();

        // Calcola il parametro u dell'intersezione del raggio con il triangolo
        float u = tvec.dot(pvec, Vec.class) * invDet;

        // Controlla se il parametro u è fuori dai limiti del triangolo
        if (u < 0 || u > 1) {return null;}

        // Calcola il prodotto vettoriale tra il vettore tvec e il primo lato del triangolo
        Vec qvec = tvec.cross(e1);

        // Calcola il parametro v dell'intersezione del raggio con il triangolo
        float v = iray.dir.dot(qvec, Vec.class) * invDet;

        // Controlla se il parametro v è fuori dai limiti del triangolo
        if (v < 0 || u + v > 1) {return null;}

        // Calcola il parametro t dell'intersezione del raggio con il triangolo
        float t = e2.dot(qvec, Vec.class) * invDet;

        // Controlla se l'intersezione è dietro il punto di origine del raggio o oltre il limite massimo
        if (t < iray.tMin || t > iray.tMax) {return null;}

        // Calcola il punto di intersezione nel sistema di coordinate del mondo
        Point point = iray.at(t);

        // Applica la trasformazione al punto di intersezione e alla normale
        point = (Point) transformation.times(point);
        Normal normal = (Normal) transformation.times(norm);

        // Calcola le coordinate di superficie per il punto di intersezione
        Vec2d surfacePoint = calculateSurfacePoint(point);
        return new HitRecord(point, normal, surfacePoint, t, ray, this);
    }
        public Normal calculateNormal() {
            // Calcola il vettore normale al piano del triangolo utilizzando il prodotto vettoriale tra due lati
            e1 = v1.minus(v0);
            e2 = v2.minus(v0);
            norm = (e1.cross(e2)).toNormal();
            norm.normalize();
            return norm;
        }

        private float calculateTriangleArea(Vec p0, Vec p1, Vec p2) {
            // Calcola l'area del triangolo utilizzando il prodotto vettoriale tra due lati
            Vec side1 = p1.minus(p0);
            Vec side2 = p2.minus(p0);
            return side1.dot(side2, Vec.class);
    }

    public Vec2d calculateSurfacePoint(Point hit){
        // Calcola le coordinate di superficie (uv) dei vertici.
        Vec2d uv0 = new Vec2d(0.0f, 0.0f);
        Vec2d uv1 = new Vec2d(1.0f, 0.0f);
        Vec2d uv2 = new Vec2d(0.0f, 1.0f);

        // Calcola i pesi w0, w1, w2 usando la formula del baricentro.

        float areaABC = calculateTriangleArea(v0, v1, v2);
        float areaPBC = calculateTriangleArea(hit.toVec(), v1, v2);
        float areaPCA = calculateTriangleArea(v0, hit.toVec(), v2);
        float areaPAB = calculateTriangleArea(v0, v1, hit.toVec());

        // Calcola i pesi dividendo le aree relative.
        float w0 = areaPBC / areaABC;
        float w1 = areaPCA / areaABC;
        float w2 = areaPAB / areaABC;

        // Calcola le coordinate di superficie (uv) come combinazioni lineari dei vertici.
        float u = w0 * uv0.u + w1 * uv1.u + w2 * uv2.u;
        float v = w0 * uv0.v + w1 * uv1.v + w2 * uv2.v;

        return new Vec2d(u, v);
    }

}


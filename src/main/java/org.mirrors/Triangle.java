package org.mirrors;


import java.util.List;

public class Triangle extends Shape {
    public Point v0;  // Vertice 0
    public Point v1;  // Vertice 1
    public Point v2;  // Vertice 2
    public Vec e1;  // lato 1
    public Vec e2;  // lato 2
    public Normal norm;
    public Triangle(Transformation transformation) {
        super(transformation);
    }
    public Triangle(Material material) {
        super(material);
    }
    public Triangle(Point v0, Point v1, Point v2, Transformation transformation, Material material) {
        super(transformation, material);
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        norm = calculateNormal();

    }

    public Triangle(Point v0, Point v1, Point v2, Material material) {
        super(material);
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        norm = calculateNormal();
    }

    public Triangle(Point v0, Point v1, Point v2) {
        this.v0 = v0;
        this.v1 = v1;
        this.v2 = v2;
        norm = calculateNormal();
    }


    public HitRecord rayIntersection(Ray ray) {
        // Applica la trasformazione inversa al raggio
        Ray iray = this.transformation.inverse().times(ray);

        if (isNormalOrientationCorrect(iray)) {
            norm = (Normal) norm.neg();
        }

        // Prodotto vettoriale tra direzione del raggio e il secondo lato del triangolo
        Vec pvec = iray.dir.cross(e2);

        // Calcola determinante tra il lato del triangolo e pvec
        float det = e1.dot(pvec);

        // Condizione di parallelismo
        if (Math.abs(det) < 1e-5) {
            return null;
        }

        float invDet = 1.0f / det;

        // Calcola il vettore dal punto di origine del raggio al primo vertice del triangolo
        Vec tvec = iray.origin.minus(v0);

        // Calcola il parametro u dell'intersezione del raggio con il triangolo
        float u = tvec.dot(pvec) * invDet;

        // Controlla se il parametro u è fuori dai limiti del triangolo
        if (u < 0 || u > 1) {
            return null;
        }

        // Calcola il prodotto vettoriale tra il vettore tvec e il primo lato del triangolo
        Vec qvec = tvec.cross(e1);

        // Calcola il parametro v dell'intersezione del raggio con il triangolo
        float v = iray.dir.dot(qvec) * invDet;

        // Controlla se il parametro v è fuori dai limiti del triangolo
        if (v < 0 || u + v > 1) {
            return null;
        }

        // Calcola il parametro t dell'intersezione del raggio con il triangolo
        float t = e2.dot(qvec) * invDet;

        // Controlla se l'intersezione è dietro il punto di origine del raggio o oltre il limite massimo
        if (t < iray.tMin || t > iray.tMax) {
            return null;
        }

        // Calcola il punto di intersezione nel sistema di coordinate del mondo
        Point point = iray.at(t);

        // Applica la trasformazione al punto di intersezione e alla normale
        point = (Point) transformation.times(point);
        norm = (Normal) transformation.times(norm);
        norm.normalize();

        // Calcola le coordinate di superficie per il punto di intersezione
        Vec2d surfacePoint = calculateSurfacePoint(point);
        return new HitRecord(point, norm, surfacePoint, t, ray, this);
    }

    public Normal calculateNormal() {
        // Calcola il vettore normale al piano del triangolo utilizzando il prodotto vettoriale tra due lati
        e1 = v1.minus(v0);
        e2 = v2.minus(v0);
        norm = (e1.cross(e2)).toNormal();
        norm.normalize();
        return norm;
    }

    private boolean isNormalOrientationCorrect(Ray iray) {
        return norm.dot(iray.dir) > 0;
    }

    public float calculateTriangleArea(Point p0, Point p1, Point p2) {
        // Calcola l'area del triangolo utilizzando il prodotto vettoriale tra due lati
        Vec side1 = p1.minus(p0);
        Vec side2 = p2.minus(p0);
        return side1.cross(side2).module() * 0.5f;
    }

    public Vec2d calculateSurfacePoint(Point hit) {
        // Calcola le coordinate di superficie (uv) dei vertici.
        Vec2d uv0 = new Vec2d(0.0f, 0.0f);
        Vec2d uv1 = new Vec2d(1.0f, 0.0f);
        Vec2d uv2 = new Vec2d(0.0f, 1.0f);

        // Calcola i pesi w0, w1, w2 usando la formula del baricentro.
        float areaABC = calculateTriangleArea(v0, v1, v2);
        float areaPv1v2 = calculateTriangleArea(hit, v1, v2);
        float areaPv0v2 = calculateTriangleArea(hit, v0, v2);
        float areaPv0v1 = calculateTriangleArea(hit, v0, v1);

        // Calcola i pesi dividendo le aree relative.
        float w0 = areaPv1v2 / areaABC;
        float w1 = areaPv0v2 / areaABC;
        float w2 = areaPv0v1 / areaABC;

        // Calcola le coordinate di superficie (uv) come combinazioni lineari dei vertici.
        float u = w0 * uv0.u + w1 * uv1.u + w2 * uv2.u;
        float v = w0 * uv0.v + w1 * uv1.v + w2 * uv2.v;

        return new Vec2d(u, v);
    }

    public List<HitRecord> rayIntersectionList(Ray ray) {
        return null;
    }

    public boolean isInternal(Point point) {
        return false;
    }


}

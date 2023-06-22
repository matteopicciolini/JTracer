package org.mirrors;
import java.util.ArrayList;
import java.util.List;

public class TriangleMesh extends Shape{
    public List<Triangle> triangles;
    public List<Vec> vertices;

    public TriangleMesh(ArrayList vertices, Material material) {
        triangles = new ArrayList<>();
        this.vertices = vertices;
        this.material = material;
        createTetrahedron();
    }
    public TriangleMesh(ArrayList vertices) {
        triangles = new ArrayList<>();
        this.vertices = vertices;
        createTetrahedron();
    }

    public void createTetrahedron() {


        // Creazione dei triangoli del tetraedro
        Triangle t0 = new Triangle(vertices.get(0), vertices.get(1), vertices.get(2), material);
        Triangle t1 = new Triangle(vertices.get(0), vertices.get(2), vertices.get(3), material);
        Triangle t2 = new Triangle(vertices.get(0), vertices.get(3), vertices.get(1), material);
        Triangle t3 = new Triangle(vertices.get(1), vertices.get(3), vertices.get(2), material);

        // Aggiunta dei triangoli alla lista
        triangles.add(t0);
        triangles.add(t1);
        triangles.add(t2);
        triangles.add(t3);
    }

    public HitRecord rayIntersection(Ray ray) throws InvalidMatrixException {
        HitRecord closestHit = null;
        for (Triangle triangle : triangles) {
            HitRecord hit = triangle.rayIntersection(ray);
            if (hit != null) {
                if (closestHit == null || hit.t < closestHit.t) {
                    closestHit = hit;
                }
            }
        }
        return closestHit;
    }
}
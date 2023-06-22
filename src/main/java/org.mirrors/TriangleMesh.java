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


        int numVertices = vertices.size();

        // Genera i triangoli
        for (int i = 0; i < numVertices; i++) {
            Vec v0 = vertices.get(i);

            for (int j = i + 1; j < numVertices; j++) {
                Vec v1 = vertices.get(j);

                for (int k = j + 1; k < numVertices; k++) {
                    Vec v2 = vertices.get(k);

                    Triangle triangle = new Triangle(v0, v1, v2, material);
                    triangles.add(triangle);
                }
            }
        }
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
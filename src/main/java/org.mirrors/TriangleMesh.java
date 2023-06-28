package org.mirrors;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class TriangleMesh extends Shape{
    public ArrayList<Triangle> triangles;
    public ArrayList<Point> vertices;

    public TriangleMesh(ArrayList vertices, Material material) {
        super(material);
        triangles = new ArrayList<>();
        this.vertices = vertices;

        createGenericMesh();
    }
    public TriangleMesh(Material material) {
        super(material);
        this.vertices = new ArrayList<>();
        triangles = new ArrayList<>();


    }
    public TriangleMesh(Transformation trans) {
        super(trans);
        this.vertices = new ArrayList<>();
        triangles = new ArrayList<>();

    }
    public TriangleMesh(Material material, Transformation transformation) {
        super(transformation, material);
        this.vertices = new ArrayList<>();
        triangles = new ArrayList<>();

    }
    public TriangleMesh(ArrayList<Point> vertices, Material material, Transformation transformation) {
        super(transformation, material);
        this.vertices = vertices;
        triangles = new ArrayList<>();
        createGenericMesh();
    }
    public TriangleMesh(ArrayList vertices) {
        triangles = new ArrayList<>();
        this.vertices = vertices;
        createGenericMesh();
    }

    public void createFileShape(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(fileName)))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("v ")) {
                    // Parse vertex coordinates
                    String[] vertexData = line.split(" ");
                    float x = Float.parseFloat(vertexData[1]);
                    float y = Float.parseFloat(vertexData[2]);
                    float z = Float.parseFloat(vertexData[3]);
                    Point vertex = new Point(x, y, z);

                    vertices.add(vertex);

                } else if (line.startsWith("f ")) {
                    // Parse triangle vertices
                    String[] faceData = line.split(" ");
                    int v0Index = Integer.parseInt(faceData[1]) - 1;
                    int v1Index = Integer.parseInt(faceData[2]) - 1;
                    int v2Index = Integer.parseInt(faceData[3]) - 1;

                    Point v0 = vertices.get(v0Index);
                    Point v1 = vertices.get(v1Index);
                    Point v2 = vertices.get(v2Index);
                    Triangle triangle = new Triangle(v0, v1, v2, transformation, material);

                    triangles.add(triangle);

                }
            }

    } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createGenericMesh() {
        int numVertices = vertices.size();

        // Genera i triangoli
        for (int i = 0; i < numVertices - 2; i++) {
            for (int j = i + 1; j < numVertices - 1; j++) {
                for (int k = j + 1; k < numVertices; k++) {
                    Triangle triangle = new Triangle(vertices.get(k), vertices.get(j), vertices.get(i), transformation, material);
                    triangles.add(triangle);
                }
            }
        }
    }

        public HitRecord rayIntersection(Ray ray) throws InvalidMatrixException {
        Box aabb = AABB(vertices);
        Ray iray = this.transformation.inverse().times(ray);
        // Condizione di intersezione box AABB
        //if (aabb.rayIntersection(ray)==null) {
           //return null;}

        HitRecord closestHit = null;
        for (Triangle triangle : triangles) {
            HitRecord hit = triangle.rayIntersection(iray);
            if (hit != null) {
                if (closestHit == null || hit.t < closestHit.t) {
                    closestHit = hit;
                }
            }
        }
        return closestHit;
    }



    /** Genera un box ceh racchiude completamente la figura in modo da effettuare un controllo
     * in rayInterception prima di calcolare tutte le normali e le intersezioni dei triangoli*/

    public Box AABB(ArrayList<Point> vertices){
        float minX = 0;
        float minY = 0;
        float minZ = 0;
        float maxX = 0;
        float maxY = 0;
        float maxZ = 0;

        for (Point point : vertices) {
            float x = point.x;
            float y = point.y;
            float z = point.z;

            if (x < minX) minX = x;
            if (y < minY) minY = y;
            if (z < minZ) minZ = z;
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
            if (z > maxZ) maxZ = z;
        }

        Point min = new Point(minX, minY, minZ);
        Point max = new Point(maxX, maxY, maxZ);
        return new Box(min, max, transformation);
    }
    public void tetrahedron(){
        this.vertices.add(new Point(0, 0.2f, 0));
        this.vertices.add(new Point(0, -0.2f, 0));
        this.vertices.add(new Point(-0.3f, 0, 0));
        this.vertices.add(new Point(-0.25f, 0, 0.3f));
        for(int i=0; i< vertices.size(); i++)
            this.vertices.set(i, (Point) transformation.times(this.vertices.get(i)));
        createGenericMesh();
    }

    public void octahedron(){
        this.vertices.add(new Point(-0.2f, -0.2f, 0.25f));
        this.vertices.add(new Point(0.2f, -0.2f, 0.25f));
        this.vertices.add(new Point(-0.2f, 0.2f, 0.25f));
        this.vertices.add(new Point(0.2f, 0.2f, 0.25f));
        this.vertices.add(new Point(0f, 0, 0.5f));
        this.vertices.add(new Point(0f, 0, 0.0f));
        for(int i=0; i< vertices.size(); i++)
            this.vertices.set(i, (Point) transformation.times(this.vertices.get(i)));
        createGenericMesh();
    }
    /** generate a dodecahedron with right translation and dilatation to be seen as well in the figure
     * Optimal visualization with rescaling Vec(0.3f, 0.3f, 0.3f)*/
    public void dodecahedron(){
        float phi = (float) ((1 + Math.sqrt(5)) / 2); // Rapporto aureo

// Aggiungi i vertici del dodecaedro
        vertices.add(new Point(-1, 1, 1));
        vertices.add(new Point(1, 1, 1));
        vertices.add(new Point(1, -1, 1));
        vertices.add(new Point(-1, -1, 1));
        vertices.add(new Point(-1, 1, -1));
        vertices.add(new Point(1, 1, -1));
        vertices.add(new Point(1, -1, -1));
        vertices.add(new Point(-1, -1, -1));
        vertices.add(new Point(0, 1 / phi, phi));
        vertices.add(new Point(0, 1 / phi, -phi));
        vertices.add(new Point(0, -1 / phi, phi));
        vertices.add(new Point(0, -1 / phi, -phi));
        vertices.add(new Point(1 / phi, phi, 0));
        vertices.add(new Point(1 / phi, -phi, 0));
        vertices.add(new Point(-1 / phi, phi, 0));
        vertices.add(new Point(-1 / phi, -phi, 0));
        vertices.add(new Point(phi, 0, 1 / phi));
        vertices.add(new Point(-phi, 0, 1 / phi));
        vertices.add(new Point(phi, 0, -1 / phi));
        vertices.add(new Point(-phi, 0, -1 / phi));
        for(int i=0; i< vertices.size(); i++)
            this.vertices.set(i, (Point) transformation.times(this.vertices.get(i)));
        createGenericMesh();
    }

    /** generate an icosahedron with right translation and dilatation to be seen as well in the figure
     * Optimal visualization with rescaling Vec(0.3f, 0.3f, 0.3f)*/

    public void icosahedron(){
        float phi = (float) ((1 + Math.sqrt(5)) / 2); // Rapporto aureo
        vertices.add(new Point(0, 1 , phi));
        vertices.add(new Point(0, -1, -phi));
        vertices.add(new Point(0, -1, phi));
        vertices.add(new Point(0, 1, -phi));
        vertices.add(new Point(1, phi, 0));
        vertices.add(new Point(1, -phi, 0));
        vertices.add(new Point(-1, phi, 0));
        vertices.add(new Point(-1, -phi, 0));
        vertices.add(new Point(phi, 0, 1 ));
        vertices.add(new Point(-phi, 0, 1 ));
        vertices.add(new Point(phi, 0, -1 ));
        vertices.add(new Point(-phi, 0, -1));
        for(int i=0; i< vertices.size(); i++)
            this.vertices.set(i, (Point) transformation.times(this.vertices.get(i)));
        createGenericMesh();
    }
    public void diamond(){

        vertices.add(new Point(0, 0, 0.78f));
        vertices.add(new Point(0.45f, 0.45f, 0));
        vertices.add(new Point(0.45f, -0.45f, 0));
        vertices.add(new Point(-0.45f, -0.45f,0));
        vertices.add(new Point(-0.45f, 0.45f, 0));
        vertices.add(new Point(0, 0, -0.78f+1));
        for(int i=0; i< vertices.size(); i++)
            this.vertices.set(i, (Point) transformation.times(this.vertices.get(i)));
        createGenericMesh();
    }
}
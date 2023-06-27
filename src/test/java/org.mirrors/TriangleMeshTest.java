package org.mirrors;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.*;

public class TriangleMeshTest {
    @Test
    void rayIntersection() throws InvalidMatrixException {
        Ray ray1 = new Ray(new Point(0.f, 0.f, 10f), InvVecZ);
        ArrayList<Point> meshlist = new ArrayList<>();
        meshlist.add(new Point(1, 0.5f, 0.f));
        meshlist.add(new Point(-1, 0.5f, 0));
        meshlist.add(new Point(0, -1, 0));
        meshlist.add(new Point(0, 0, 1));

        TriangleMesh tetra = new TriangleMesh(meshlist);

        HitRecord intersection1 = tetra.rayIntersection(ray1);
        assertNotNull(intersection1);

        //HitRecord trueIntersection1 = new HitRecord(new Point(0.f, 0.f, 0.0f),
              //  new Normal(0.f, 0.f, 1.f),
                //new Vec2d(1.f, 0.875f),
                //1.f, ray1, new Triangle(v0, v1, v2));
        //assertTrue(intersection1.isClose());
        System.out.println(intersection1.worldPoint);
    }
    @Test
    void aabb() throws InvalidMatrixException {
        Ray ray1 = new Ray(new Point(0.1f, 0.1f, 10f), InvVecZ);
        ArrayList<Point> meshlist = new ArrayList<>();
        meshlist.add(new Point(1, 0.5f, 0.f));
        meshlist.add(new Point(-1, 0.5f, 0));
        meshlist.add(new Point(0, -1, 0));
        meshlist.add(new Point(0, 0, 1));
        TriangleMesh tetra = new TriangleMesh(meshlist);

        Box aabb = tetra.AABB(tetra.vertices);
        HitRecord inter1 = aabb.rayIntersection(ray1);
        HitRecord inter2 = tetra.rayIntersection(ray1);

        assertEquals(inter1.worldPoint.x, inter2.worldPoint.x);
        assertEquals(inter1.worldPoint.y, inter2.worldPoint.y);
        assertNotEquals(inter1.worldPoint.z, inter2.worldPoint.z);
    }
        @Test
        void normal(){
            ArrayList<Point> vertices = new ArrayList<>();
            vertices.add(new Point(-0.7f, 0.00f, 0));
            vertices.add(new Point(-0.6f, -0.1f, 0));
            vertices.add(new Point(-0.65f, 0, 0f));
            vertices.add(new Point(-0.55f, 0.07f, 0.0f));
            //vertices.add(new Point(-0.75f, 0.05f, 0.2f));
            vertices.add(new Point(-0.7f, -0.05f, 0.f));


            TriangleMesh tetra = new TriangleMesh(vertices);

            for(int i=0; i<tetra.triangles.size(); i++){
                System.out.println(tetra.triangles.get(i).norm.z);

                System.out.println(i);
        }

    }
    @Test
    void createFileShape() throws InvalidMatrixException {
        Ray ray1 = new Ray(new Point(1f, 1f, 10f), InvVecZ);
        Material green =  new Material(new DiffuseBRDF(new UniformPigment(Green)));

        Transformation scal= Transformation.scaling(new Vec(0.5f, 0.5f, 0.5f));
        TriangleMesh mesh2= new TriangleMesh(green);
        mesh2.createFileShape("tetra.txt");
        HitRecord int1= mesh2.rayIntersection(ray1);
        System.out.println(int1.worldPoint);

        assertTrue(int1.worldPoint.isClose(new Point(1, 1, 1)));

    }



}


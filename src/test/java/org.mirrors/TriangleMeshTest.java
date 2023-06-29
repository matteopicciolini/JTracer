package org.mirrors;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.*;

public class TriangleMeshTest {
    @Test
    void rayIntersection() throws InvalidMatrixException {
        Ray ray1 = new Ray(new Point(1.f, 1.f, 10f), InvVecZ);
        Ray ray2 = new Ray(new Point(-10.f, 1.f, 1f), VecX);
        Ray ray3 = new Ray(new Point(1.f, -10.f, 1f), VecY);

        ArrayList<Point> meshlist = new ArrayList<>();

        meshlist.add(new Point(0, 0f, 0f));
        meshlist.add(new Point(3, 0f, 0));
        meshlist.add(new Point(0, 3, 0));
        meshlist.add(new Point(0, 0, 3));

        TriangleMesh tetra = new TriangleMesh(meshlist);

        HitRecord intersection1 = tetra.rayIntersection(ray1);
        HitRecord intersection2 = tetra.rayIntersection(ray2);
        HitRecord intersection3 = tetra.rayIntersection(ray3);

        assertNotNull(intersection1);
        assertNotNull(intersection2);
        assertNotNull(intersection3);

        assertTrue(intersection1.worldPoint.isClose(new Point(1.0f, 1.0f, 1.0f)));
        assertTrue(intersection2.worldPoint.isClose(new Point(0.0f, 1.0f, 1.0f)));
        assertTrue(intersection3.worldPoint.isClose(new Point(1.0f, 0.0f, 1.0f)));



    }
    @Test
    void rayIntersectionSingleTriangle() throws InvalidMatrixException {

        Transformation rot = Transformation.rotationZ(-90);
        Transformation transl = Transformation.translation(new Vec(1, 0, 0));
        Transformation scal = Transformation.scaling(new Vec(0.21f, 0.21f, 0.21f));
        Material green =  new Material(new DiffuseBRDF(new UniformPigment(Green)));

        TriangleMesh fileS= new TriangleMesh(green, scal);
        TriangleMesh fileR= new TriangleMesh(green, rot);
        TriangleMesh fileT= new TriangleMesh(green, transl);
        fileS.createFileShape("tetra2x.txt");
        fileR.createFileShape("tetra2x.txt");
        fileT.createFileShape("tetra2x.txt");

        Ray rayx = new Ray(new Point(10.f, 0.1f, 0.1f), InvVecX);
        Ray rayy = new Ray(new Point(0.1f, 10f, 0.1f), InvVecY);

        HitRecord intersection = fileS.rayIntersection(rayx);
        assertTrue(intersection.worldPoint.isClose(new Point(0, 0.1f, 0.1f)));

        intersection = fileR.rayIntersection(rayy);
        assertTrue(intersection.worldPoint.isClose(new Point(0.1f, 0f, 0.1f)));

        intersection = fileT.rayIntersection(rayx);
        assertTrue(intersection.worldPoint.isClose(new Point(1f, 0.1f, 0.1f)));

    }

    @Test
    void rayIntersectionTetra() throws InvalidMatrixException {

        Transformation rot = Transformation.rotationZ(90);
        Transformation transl = Transformation.translation(new Vec(1, 1, 1));
        Transformation scal = Transformation.scaling(new Vec(0.21f, 0.21f, 0.21f));
        Material green =  new Material(new DiffuseBRDF(new UniformPigment(Green)));

        TriangleMesh file= new TriangleMesh(green);
        TriangleMesh fileS= new TriangleMesh(green, scal);
        TriangleMesh fileR= new TriangleMesh(green, rot);
        TriangleMesh fileT= new TriangleMesh(green, transl);
        fileS.createFileShape("tetra.txt");
        fileR.createFileShape("tetra.txt");
        fileT.createFileShape("tetra.txt");
        file.createFileShape("tetra.txt");

        Ray rayx = new Ray(new Point(-10f, 0.1f, 0.1f), VecX);
        Ray rayy = new Ray(new Point(0.1f, -10f, 0.1f), VecY);
        Ray rayz = new Ray(new Point(0.1f, 0.1f, -10f), VecZ);
        Ray ray0 = new Ray(new Point(10f, 0.1f, 0.1f), InvVecX);
        Ray ray01 = new Ray(new Point(10f, 1.1f, 1.1f), InvVecX);

        HitRecord intersection0 = file.rayIntersection(ray0);
        intersection0 = fileS.rayIntersection(ray0);

        HitRecord intersection = fileR.rayIntersection(rayz);
        assertNull(intersection);
        intersection = fileR.rayIntersection(rayy);
        assertNull(intersection);
        intersection = fileR.rayIntersection(rayx);
        assertNotNull(intersection);
        assertTrue(intersection.worldPoint.isClose(new Point(-0.8f, 0.1f, 0.1f)));

        intersection = fileS.rayIntersection(rayz);
        assertNotNull(intersection);
        assertTrue(intersection.worldPoint.isClose(new Point(0.1f, 0.1f, 0f)));
        intersection = fileS.rayIntersection(rayy);
        assertNotNull(intersection);
        assertTrue(intersection.worldPoint.isClose(new Point(0.1f, 0f, 0.1f)));
        intersection = fileS.rayIntersection(rayx);
        assertNotNull(intersection);
        assertTrue(intersection.worldPoint.isClose(new Point(0.f, 0.1f, 0.1f)));

        rayx = new Ray(new Point(-10f, 1.1f, 1.1f), VecX);
        rayy = new Ray(new Point(1.1f, -10f, 1.1f), VecY);
        rayz = new Ray(new Point(1.1f, 1.1f, -10f), VecZ);
        intersection = fileT.rayIntersection(rayz);
        assertNotNull(intersection);
        assertTrue(intersection.worldPoint.isClose(new Point(1.1f, 1.1f, 1f)));
        intersection = fileT.rayIntersection(rayy);
        assertNotNull(intersection);
        assertTrue(intersection.worldPoint.isClose(new Point(1.1f, 1f, 1.1f)));
        intersection = fileT.rayIntersection(rayx);
        assertNotNull(intersection);
        assertTrue(intersection.worldPoint.isClose(new Point(1.f, 1.1f, 1.1f)));


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

}


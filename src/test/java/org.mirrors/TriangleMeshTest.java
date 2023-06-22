package org.mirrors;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mirrors.Global.*;

public class TriangleMeshTest {
    @Test
    void rayIntersection() throws InvalidMatrixException {
        Ray ray1 = new Ray(new Point(0.f, 0.f, 10f), InvVecZ);
        ArrayList<Vec> meshlist = new ArrayList<>();
        meshlist.add(new Vec(1, 0.5f, 0.f));
        meshlist.add(new Vec(-1, 0.5f, 0));
        meshlist.add(new Vec(0, -1, 0));
        meshlist.add(new Vec(0, 0, 1));

        TriangleMesh tetra = new TriangleMesh(meshlist);
        tetra.createTetrahedron();
        HitRecord intersection1 = tetra.rayIntersection(ray1);
        assertNotNull(intersection1);

        //HitRecord trueIntersection1 = new HitRecord(new Point(0.f, 0.f, 0.0f),
              //  new Normal(0.f, 0.f, 1.f),
                //new Vec2d(1.f, 0.875f),
                //1.f, ray1, new Triangle(v0, v1, v2));
        //assertTrue(intersection1.isClose());
        System.out.println(intersection1.worldPoint);
    }

}


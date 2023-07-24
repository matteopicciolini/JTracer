package org.mirrors;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class PermutationsTest {
    @Test
    void generatePermutations() {
        ArrayList<Point> vertices = new ArrayList<>();
        vertices.add(new Point(0.1f, 0.05f, 0.1f));
        vertices.add(new Point(-0.1f, 0.05f, 0.1f));
        vertices.add(new Point(0, -0.1f, 0.1f));
        vertices.add(new Point(0, 0, 0.2f));
        vertices.add(new Point(3, 2, 0.5f));

        ArrayList<Triangle> tris = new ArrayList<>();
        int c = 0;
        for (int i = 0; i < vertices.size() - 2; i++) {
            for (int j = i + 1; j < vertices.size() - 1; j++) {
                for (int k = j + 1; k < vertices.size(); k++) {

                    tris.add(new Triangle(vertices.get(i), vertices.get(j), vertices.get(k)));
                    c++;
                }
            }
        }
    }
}

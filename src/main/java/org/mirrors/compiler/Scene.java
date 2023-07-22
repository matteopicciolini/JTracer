package org.mirrors.compiler;

import org.mirrors.Camera;
import org.mirrors.Material;
import org.mirrors.Shape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Scene {
    public Map<String, Material> materials = new HashMap<>();
    public List<Shape> objects = new ArrayList<>();
    public Camera camera;
    public Map<String, Float> floatVariables = new HashMap<>();

    public Scene(Camera camera) {
        this.camera = camera;
    }

    public Scene() {
    }
}
package org.mirrors.compiler;
import org.mirrors.*;

import java.util.*;

public class Scene {
    public Map<String, Material> materials = new HashMap<>();
    public List<Shape> objects = new ArrayList<>();
    public Camera camera;
    public Map<String, Float> floatVariables = new HashMap<>();
    public Scene(Camera camera){
        this.camera = camera;
    }
    public Scene(){}
}

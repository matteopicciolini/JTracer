package org.mirrors.compiler;
import org.mirrors.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Scene {
    public Map<String, Material> materials = new HashMap<>();
    public World world = new World();
    public Camera camera;
    public Map<String, Float> floatVariables = new HashMap<>();
    public Set<String> overriddenVariables = new HashSet<>();

    public Scene(World world, Camera camera){
        this.world = world;
        this.camera = camera;
    }
}

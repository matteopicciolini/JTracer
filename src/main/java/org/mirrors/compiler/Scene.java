package org.mirrors.compiler;

import org.mirrors.Material;
import org.mirrors.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Scene {
    private Map<String, Material> materials = new HashMap<>();
    private World world = new World();
    private Camera camera = null;
    private Map<String, Float> floatVariables = new HashMap<>();
    private Set<String> overriddenVariables = new HashSet<>();
}

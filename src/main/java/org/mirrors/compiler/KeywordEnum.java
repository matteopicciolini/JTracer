package org.mirrors.compiler;
import java.util.HashMap;
import java.util.Map;
public class KeywordEnum {



    public enum Keywords {;
        public static  int NEW = 1;
        public static  int MATERIAL = 2;
        public static  int PLANE = 3;
        public static  int SPHERE = 4;
        public static  int DIFFUSE = 5;
        public static  int SPECULAR = 6;
        public static  int UNIFORM = 7;
        public static  int CHECKERED = 8;
        public static  int IMAGE = 9;
        public static  int IDENTITY = 10;
        public static  int TRANSLATION = 11;
        public static  int ROTATION_X = 12;
        public static  int ROTATION_Y = 13;
        public static  int ROTATION_Z = 14;
        public static  int SCALING = 15;
        public static  int CAMERA = 16;
        public static  int ORTHOGONAL = 17;
        public static  int PERSPECTIVE = 18;
        public static  int FLOAT = 19;
        public static  int POINT_LIGHT = 20;

        public static final Map<String, Integer> KEYWORDS = new HashMap<>();

        static {
            KEYWORDS.put("new", NEW);
            KEYWORDS.put("material", MATERIAL);
            KEYWORDS.put("plane", PLANE);
            KEYWORDS.put("sphere", SPHERE);
            KEYWORDS.put("diffuse", DIFFUSE);
            KEYWORDS.put("specular", SPECULAR);
            KEYWORDS.put("uniform", UNIFORM);
            KEYWORDS.put("checkered", CHECKERED);
            KEYWORDS.put("image", IMAGE);
            KEYWORDS.put("identity", IDENTITY);
            KEYWORDS.put("translation", TRANSLATION);
            KEYWORDS.put("rotation_x", ROTATION_X);
            KEYWORDS.put("rotation_y", ROTATION_Y);
            KEYWORDS.put("rotation_z", ROTATION_Z);
            KEYWORDS.put("scaling", SCALING);
            KEYWORDS.put("camera", CAMERA);
            KEYWORDS.put("orthogonal", ORTHOGONAL);
            KEYWORDS.put("perspective", PERSPECTIVE);
            KEYWORDS.put("float", FLOAT);
            KEYWORDS.put("point_light", POINT_LIGHT);
        }
    }
}




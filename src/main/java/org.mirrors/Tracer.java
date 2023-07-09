package org.mirrors;
import org.apache.commons.cli.*;
import java.io.*;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.mirrors.Global.*;

import java.io.File;

public class Tracer {
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption("h", "help", false, "display help information");

        options.addOption(Option.builder("p")
                .argName("fileInput.pfm> <[fileOutput.png]> <[gamma]> <[factor]")
                .hasArgs()
                .longOpt("pfm2image")
                .valueSeparator(' ')
                .optionalArg(true)
                .desc("convert PFM file in PNG")
                .build());

        options.addOption(Option.builder("d")
                .argName("[width]> <[height]> <[angle-deg]> <[orthogonal]> <[output.pfm]> <[algorithm]")
                .hasArgs()
                .longOpt("demo")
                .valueSeparator(' ')
                .optionalArg(true)
                .desc("run DEMO")
                .build());

        options.addOption(Option.builder("r")
                .argName("[width]> <[height]> <[angle-deg]> <[orthogonal]> <[output.pfm]> <[algorithm]")
                .hasArgs()
                .longOpt("render")
                .valueSeparator(' ')
                .optionalArg(true)
                .desc("render")
                .build());

        CommandLineParser parser = new DefaultParser();

        HelpFormatter formatter = new HelpFormatter();
        formatter.setWidth(100);
        formatter.setOptionComparator(null);
        formatter.setDescPadding(4);
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption("h")) {
                formatter.printHelp("tracer", options);
            }

            if (cmd.hasOption("p")) {
                String[] files = cmd.getOptionValues("p");
                String inputFile = files[0];
                String outputFile = inputFile.substring(0, inputFile.length() - 3) + "png";
                float gamma = 2.2f;
                float factor = 0.18f;

                if (files.length >= 2 && files.length <= 4) outputFile = files[1];
                if (files.length >= 3 && files.length <= 4) gamma = parseFloat(files[2]);
                if (files.length == 4) factor = parseFloat(files[3]);
                if (files.length > 4) {
                    System.err.println("Error: ");
                    formatter.printHelp("Tracer", options);
                }
                pfm2image(factor, gamma, inputFile, outputFile);
            }

            if (cmd.hasOption("d")) {
                String[] dArgs = cmd.getOptionValues("d");

                int width = 1920;
                int height = 1080;
                float angleDeg = 0.f;
                boolean orthogonal = false;
                String fileOutput = "fileOutput";
                String algorithm = "flat";
                boolean antialiasing = false;
                boolean parallelAntialiasing = false;
                int nThreads = 4;

                if (dArgs != null) {
                    if (dArgs.length >= 1 && dArgs.length <= 9) width = parseInt(dArgs[0]);
                    if (dArgs.length >= 2 && dArgs.length <= 9) height = parseInt(dArgs[1]);
                    if (dArgs.length >= 3 && dArgs.length <= 9) angleDeg = parseInt(dArgs[2]);
                    if (dArgs.length >= 4 && dArgs.length <= 9) orthogonal = parseBoolean(dArgs[3]);
                    if (dArgs.length >= 5 && dArgs.length <= 9) fileOutput = dArgs[4];
                    if (dArgs.length >= 6 && dArgs.length <= 9) algorithm = dArgs[5];
                    if (dArgs.length >= 7 && dArgs.length <= 9) antialiasing = parseBoolean(dArgs[6]);
                    if (dArgs.length >= 8 && dArgs.length <= 9) parallelAntialiasing = parseBoolean(dArgs[7]);
                    if (dArgs.length == 9) nThreads = parseInt(dArgs[8]);
                    if (dArgs.length > 9) {
                        System.err.println("Error: ");
                        formatter.printHelp("Tracer", options);
                    }
                }
                demo(width, height, angleDeg, orthogonal, fileOutput, algorithm, antialiasing, parallelAntialiasing, nThreads);
            }

            if (cmd.hasOption("r")) {
                String[] dArgs = cmd.getOptionValues("r");

                int width = 1920;
                int height = 1080;
                float angleDeg = 0.f;
                boolean orthogonal = false;
                String fileOutput = "fileOutput";
                String algorithm = "flat";
                boolean antialiasing = false;
                boolean parallelAntialiasing = false;
                int nThreads = 4;

                if (dArgs != null) {
                    if (dArgs.length >= 1 && dArgs.length <= 9) width = parseInt(dArgs[0]);
                    if (dArgs.length >= 2 && dArgs.length <= 9) height = parseInt(dArgs[1]);
                    if (dArgs.length >= 3 && dArgs.length <= 9) angleDeg = parseInt(dArgs[2]);
                    if (dArgs.length >= 4 && dArgs.length <= 9) orthogonal = parseBoolean(dArgs[3]);
                    if (dArgs.length >= 5 && dArgs.length <= 9) fileOutput = dArgs[4];
                    if (dArgs.length >= 6 && dArgs.length <= 9) algorithm = dArgs[5];
                    if (dArgs.length >= 7 && dArgs.length <= 9) antialiasing = parseBoolean(dArgs[6]);
                    if (dArgs.length >= 8 && dArgs.length <= 9) parallelAntialiasing = parseBoolean(dArgs[7]);
                    if (dArgs.length == 8) nThreads = parseInt(dArgs[8]);
                    if (dArgs.length > 9) {
                        System.err.println("Error: ");
                        formatter.printHelp("Tracer", options);
                    }
                }
                render(width, height, angleDeg, orthogonal, fileOutput, algorithm, antialiasing, parallelAntialiasing, nThreads);
            }
        } catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            formatter.printHelp("Tracer", options);
            throw new RuntimeException(e);
        } catch (IOException | InvalidPfmFileFormatException | InvalidMatrixException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            formatter.printHelp("Tracer", options);
            throw new RuntimeException(e);
        }
    }

    public static void pfm2image(float factor, float gamma, String inputFile, String outputFile) throws IOException, InvalidPfmFileFormatException {

        Parameters param = new Parameters(factor, gamma, inputFile, outputFile);
        OutputStream out = new FileOutputStream(param.outputFileName);
        InputStream str = new FileInputStream(param.inputFileName);

        HDRImage img = PfmCreator.readPfmImage(str);

        //img.normalizeImage(param.factor);
        img.normalizeImage(param.factor, 0.5f);
        img.clampImage();
        img.writeLdrImage(out, "PNG", param.gamma);
    }

    public static void demo(int width, int height, float angleDeg, boolean orthogonal, String fileOutputPFM, String algorithm, boolean antialiasing, boolean parallelAntialiasing, int nThreads) throws InvalidMatrixException, IOException, InvalidPfmFileFormatException {
        long time = System.currentTimeMillis();


        Material skyMaterial = new Material(
                new DiffuseBRDF(new UniformPigment(Black)), new UniformPigment(White)
        );
        Material specular = new Material(new SpecularBRDF(new UniformPigment(new Color(1.f, 1.f, 1.f))));
        Material specularDarkOrange = new Material(new SpecularBRDF(new UniformPigment(DarkOrange)));
        Material DiffuseLime = new Material(new DiffuseBRDF(new UniformPigment(new Color(0.f, 0.5f, 0.f))));
        Material DiffuseYellow = new Material(new DiffuseBRDF(new UniformPigment(Yellow)));
        Material DiffuseNavy = new Material(new DiffuseBRDF(new UniformPigment(Navy)));
        Material DiffuseDarkRed = new Material(new DiffuseBRDF(new UniformPigment(DarkRed)));
        Material DiffuseOrange = new Material(new DiffuseBRDF(new UniformPigment(DarkOrange)));
        Material DiffuseIndigo = new Material(new DiffuseBRDF(new UniformPigment(Indigo)));
        Material DiffuseDarkGreen = new Material(new DiffuseBRDF(new UniformPigment(DarkGreen)));

        Material specularDarkRed = new Material(new SpecularBRDF(new UniformPigment(DarkRed)));

        Material sphereMaterial2 = new Material(new DiffuseBRDF(new UniformPigment(Yellow)));
        Material groundMaterial = new Material(
                new DiffuseBRDF(
                        new CheckeredPigment(
                                new Color(0.f, 0.5f, 0.f),
                                new Color(1f, 1f, 1f), 16
                        )
                ), new UniformPigment(Black)
        );
        Material hyperboloidmaterial = new Material(
                new DiffuseBRDF(
                        new CheckeredPigment(
                                DarkOrange,
                                new Color(1f, 1f, 1f), 16
                        )
                ), new UniformPigment(Black)
        );


        InputStream str = new FileInputStream("Moon.pfm");
        HDRImage worldImage = PfmCreator.readPfmImage(str);
        Material worldSphere = new Material(new DiffuseBRDF(new ImagePigment(worldImage), 1.f));


        Transformation rotation = Transformation.rotationZ(angleDeg);


        World world = new World();

        //SPHERES
        /*Transformation rescale = Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f));

        for (float i = -0.5f; i <= 0.5f; i += 1.0f) {
            for (float j = -0.5f; j <= 0.5f; j += 1.0f) {
                for (float k = -0.5f; k <= 0.5f; k += 1.0f) {
                    Transformation translation = Transformation.translation(new Vec(i, j, k));
                    world.addShape(new Sphere(translation.times(rescale), sphereMaterial1));
                }
            }
        }*/

        //SKY
        Transformation rescale = Transformation.scaling(new Vec(50f, 50f, 50f));
        Transformation translation = Transformation.translation(new Vec(0.f, 0.f, 0.f));
        world.addShape(new Sphere(translation.times(rescale), skyMaterial));
/*
        //CUBE
        translation = Transformation.translation(new Vec(0f, 0f, 0.042f));
        world.addShape(new Box(new Point(-0.2f,-0.2f,-0.2f), new Point(0.2f, 0.2f, 0.2f),
                translation.times(Transformation.rotationX(40).times(Transformation.rotationY(45))), DiffuseNavy));
*/
        //PLANE
        world.addShape(new Plain(Transformation.translation(new Vec(0.f, 0.f, -0.2f)), groundMaterial));/*       //SPHERE 1
        rescale = Transformation.scaling(new Vec(0.2f, 0.2f, 0.2f));
        translation = Transformation.translation(new Vec(0.f, 0.5f, 0.1f));
        world.addShape(new Sphere(translation.times(rescale), DiffuseIndigo));

        //SPHERE 2
        rescale = Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f));
        translation = Transformation.translation(new Vec(0.4f, 0.3f, 0.0f));
        world.addShape(new Sphere(translation.times(rescale), sphereMaterial2));

        // MIRROR SPHERE
        //rescale = Transformation.scaling(new Vec(0.25f, 0.2f, 0.2f));
        //translation = Transformation.translation(new Vec(0.2f, -0.5f, 0.1f));
        //world.addShape(new Sphere(translation.times(rescale), specularDarkOrange));

*/
        //CUBE
        translation = Transformation.translation(new Vec(0.25f, -0.3f, -0.13f));
        Box cube2 = new Box(new Point(-0.07f,-0.07f,-0.07f), new Point(0.07f, 0.07f, 0.07f),
                translation, specular);
        Box cube3 = new Box(new Point(-0.07f,-0.07f,-0.07f), new Point(0.07f, 0.07f, 0.07f),
                translation.times(Transformation.rotationZ(45).times(Transformation.rotationY(45))), hyperboloidmaterial);
        world.addShape(new CSGDifference(cube2, cube3));






        //SPHERE
        rescale = Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f));
        translation = Transformation.translation(new Vec(0.2f, 0.2f, -0.1f));
        Sphere sphere2 = new Sphere(translation.times(rescale), worldSphere);
        //world.addShape(sphere2);

        //CUBE
        translation = Transformation.translation(new Vec(0.25f, 0.25f, -0.01f));
        Box cube = new Box(new Point(-0.07f,-0.07f,-0.07f), new Point(0.07f, 0.07f, 0.07f),
        translation.times(Transformation.rotationX(40).times(Transformation.rotationY(45))), hyperboloidmaterial);
        //world.addShape(cube);

        CSGDifference sphereCube = new CSGDifference(sphere2, cube);
        world.addShape(sphereCube);

        //SPHERE
        rescale = Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f));
        translation = Transformation.translation(new Vec(-0.3f, -0.2f, 0.0f));
        Sphere sphere1 = new Sphere(translation.times(rescale), specular);
        //world.addShape(sphere1);


        // CONE
        rescale = Transformation.scaling(new Vec(0.2f, 0.2f, 0.2f));
        translation = Transformation.translation(new Vec(-0.2f, -0.25f, -0.2f));
        Cone cone = new Cone(translation.times(rescale), specular, 0.5f, 2.f);
        //world.addShape(cone);

        CSGDifference diff = new CSGDifference(cone, sphere1);
        world.addShape(diff);

        //SPHERE
        rescale = Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f));
        translation = Transformation.translation(new Vec(-0.2f, 0.2f, -0.1f));
        Sphere sphere = new Sphere(translation.times(rescale), specularDarkOrange);
        //world.addShape(sphere);

        // HYPERBOLA
        Transformation rotationHyp = Transformation.rotationY(120);
        rescale = Transformation.scaling(new Vec(0.035f, 0.035f, 0.05f));
        translation = Transformation.translation(new Vec(-0.2f, 0.2f, -0.1f));
        Hyperboloid hyperboloid = new Hyperboloid(translation.times(rotationHyp.times(rescale)), hyperboloidmaterial, -2.25f, 2.25f);
        //world.addShape(hyperboloid);

/*
        // CYLINDER
        rescale = Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f));
        translation = Transformation.translation(new Vec(-0.5f, 0.15f, -0.05f));
        world.addShape(new Cylinder(translation.times(Transformation.rotationZ(40).times(Transformation.rotationX(90).times(rescale))), DiffuseLime));
*/
        CSGDifference minus = new CSGDifference(sphere, hyperboloid);
        world.addShape(minus);


        float lBox = 0.145f;
        CSGIntersection boxSphereIntersection = new CSGIntersection(
                new Box(
                        new Point(-lBox,-lBox,-lBox),
                        new Point(lBox, lBox, lBox),
                        Transformation.translation(new Vec(0f, 0f, 0.15f)),
                        DiffuseDarkRed
                ),
                new Sphere(
                        Transformation.translation(new Vec(0f, 0f, 0.15f)).times(Transformation.scaling(new Vec(0.19f, 0.19f, 0.19f))),
                        specularDarkOrange
                )
        );

        float zScale = 0.4f;
        Transformation rescaleCSG = Transformation.scaling(new Vec(0.09f, 0.09f, zScale));
        Transformation translationCSG = Transformation.translation(new Vec(0f, 0f, -0.05f));
        Transformation translationCSG2 = Transformation.translation(new Vec(0f - zScale/2, 0f, -0.05f + zScale/2));
        Transformation translationCSG3 = Transformation.translation(new Vec(0f, 0f + zScale/2, -0.05f + zScale/2));
        Transformation rotationCSG2 = Transformation.rotationY(90);
        Transformation rotationCSG3 = Transformation.rotationX(90);
        Cylinder firstCylinder = new Cylinder(
                translationCSG.times(rescaleCSG),
                hyperboloidmaterial
        );
        Cylinder secondCylinder = new Cylinder(
                translationCSG2.times(rotationCSG2.times(rescaleCSG)),
                hyperboloidmaterial
        );
        Cylinder thirdCylinder = new Cylinder(
                translationCSG3.times(rotationCSG3.times(rescaleCSG)),
                hyperboloidmaterial
        );
        CSGUnion cilinderCross2d = new CSGUnion(
                firstCylinder, secondCylinder
        );
        CSGUnion cilinderCross3d = new CSGUnion(
                cilinderCross2d, thirdCylinder
        );
        //world.addShape(boxSphereIntersection);
        //world.addShape(cilinderCross3d);
        world.addShape(new CSGDifference(new CSGDifference(new CSGDifference(boxSphereIntersection, firstCylinder), secondCylinder), thirdCylinder));
        HDRImage image = new HDRImage(width, height);
        Camera camera = orthogonal ?
                new OrthogonalCamera((float) width/height, Transformation.translation(new Vec(1.0f, 0.0f, 0.0f))) :
                new PerspectiveCamera(1f, (float) width/height, rotation.times(Transformation.translation(new Vec(0.3f, 0f, 0.1f)).times(Transformation.rotationY(20))));

        ImageTracer tracer;
        if (antialiasing == true){
            tracer = new ImageTracer(image, camera, 4, new PCG());
        }else{
            tracer = new ImageTracer(image, camera);
        }

        if(algorithm.equals("flat")){
            tracer.fireAllRays(new FlatRenderer(world));
        }
        else if (algorithm.equals("onOff")){
            tracer.fireAllRays(new OnOffRenderer(world));
        }
        else if (algorithm.equals("pathTracer")){
            if (parallelAntialiasing == true) {
                tracer.fireAllRaysParallel(new PathTracer(world), nThreads);
            }else{
                tracer.fireAllRays(new PathTracer(world));
            }
        }

        image.writePfm(new FileOutputStream(fileOutputPFM), LITTLE_ENDIAN);
        String fileOutputPNG = fileOutputPFM.substring(0, fileOutputPFM.length() - 3) + "png";
        pfm2image(0.18f, 2.5f, fileOutputPFM, fileOutputPNG);
        RemoveFile(fileOutputPFM);
        long time2 = System.currentTimeMillis();
        System.out.println(time2 - time);
    }

    public static void render(int width, int height, float angleDeg, boolean orthogonal, String fileOutputPFM, String algorithm, boolean antialiasing, boolean parallelAntialiasing, int nThreads) throws InvalidMatrixException, IOException, InvalidPfmFileFormatException {
        long time = System.currentTimeMillis();


        Material skyMaterial = new Material(
                new DiffuseBRDF(new UniformPigment(Black)), new UniformPigment(White)
        );
        Material mirrorMaterial = new Material(new SpecularBRDF(new UniformPigment(DarkOrange)));
        Material DiffuseNavy = new Material(new DiffuseBRDF(new UniformPigment(Navy)));
        Material DiffuseRed = new Material(new DiffuseBRDF(new UniformPigment(Red)));
        Material sphereMaterial2 = new Material(new DiffuseBRDF(new UniformPigment(Yellow)));
        Material groundMaterial = new Material(
                new DiffuseBRDF(
                        new CheckeredPigment(
                                new Color(0.f, 0.5f, 0.f),
                                new Color(1f, 1f, 1f), 16
                        )
                ), new UniformPigment(Black)
        );

        Transformation rotation = Transformation.rotationZ(angleDeg);

        World world = new World();

        //SKY
        Transformation rescale = Transformation.scaling(new Vec(50f, 50f, 50f));
        Transformation translation = Transformation.translation(new Vec(0.f, 0.f, 0.f));
        world.addShape(new Sphere(translation.times(rescale), skyMaterial));

        //CUBE
        //translation = Transformation.translation(new Vec(-0.5f, 0.5f, 0));
        /*world.addShape(new Box(
                new Point(-0.1f,-0.1f,-0.1f),
                new Point(0.1f, 0.1f, 0.1f),
                Transformation.translation(new Vec(-0.5f, 0.5f, 0)),
                DiffuseRed
        ));*/
        //SPHERE
        //translation = Transformation.translation(new Vec(-0.5f, 0f, 0));
        //rescale = Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f));
        /*world.addShape(new Sphere(
                Transformation.translation(new Vec(-0.5f, 0.5f, 0)).times(Transformation.scaling(new Vec(0.15f, 0.15f, 0.15f))),
                DiffuseNavy
        ));*/


        //CSG
        world.addShape(
                new CSGIntersection(
                        new Box(
                                new Point(-0.1f,-0.1f,-0.1f),
                                new Point(0.1f, 0.1f, 0.1f),
                                Transformation.translation(new Vec(-0.5f, 0.5f, 0)),
                                DiffuseRed
                        ),
                        new Sphere(
                                Transformation.translation(new Vec(-0.5f, 0.5f, 0)).times(Transformation.scaling(new Vec(0.13f, 0.13f, 0.13f))),
                                DiffuseNavy
                        )
                )
        );
        //PLANE
        //world.addShape(new Plain(Transformation.translation(new Vec(0.f, 0.f, -0.1f)), groundMaterial));



        HDRImage image = new HDRImage(width, height);
        Camera camera = orthogonal ?
                new OrthogonalCamera((float) width/height, Transformation.translation(new Vec(1.0f, 0.0f, 0.0f))) :
                new PerspectiveCamera(1f, (float) width/height, rotation.times(Transformation.translation(new Vec(-0.3f, 0f, 0.5f)).times(Transformation.rotationZ(20).times(Transformation.rotationY(20)))));

        ImageTracer tracer;
        if (antialiasing == true){
            tracer = new ImageTracer(image, camera, 4, new PCG());
        }else{
            tracer = new ImageTracer(image, camera);
        }

        if(algorithm.equals("flat")){
            tracer.fireAllRays(new FlatRenderer(world));
        }
        else if (algorithm.equals("onOff")){
            tracer.fireAllRays(new OnOffRenderer(world));
        }
        else if (algorithm.equals("pathTracer")){
            if (parallelAntialiasing == true) {
                tracer.fireAllRaysParallel(new PathTracer(world), nThreads);
            }else{
                tracer.fireAllRays(new PathTracer(world));
            }
        }

        image.writePfm(new FileOutputStream(fileOutputPFM), LITTLE_ENDIAN);
        String fileOutputPNG = fileOutputPFM.substring(0, fileOutputPFM.length() - 3) + "png";
        pfm2image(0.18f, 2.5f, fileOutputPFM, fileOutputPNG);
        RemoveFile(fileOutputPFM);
        long time2 = System.currentTimeMillis();
        System.out.println(time2 - time);
    }


    public static void RemoveFile(String fileName) {
        File file = new File(fileName);

        if (file.exists()) {
            if (file.delete()) {
                System.out.println("The file " + fileName + " has been successfully deleted.");
            } else {
                System.out.println("Unable to delete the file " + fileName +".");
            }
        } else {
            System.out.println("The file " + fileName + "does not exist.");
        }
    }
}
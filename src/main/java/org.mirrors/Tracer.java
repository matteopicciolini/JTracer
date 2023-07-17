package org.mirrors;

import com.sun.jdi.connect.TransportTimeoutException;
import org.mirrors.compiler.GrammarErrorException;
import org.mirrors.compiler.InStream;
import org.mirrors.compiler.Scene;
import java.io.*;
import java.util.ArrayList;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.mirrors.Global.*;

public class Tracer {
    public static void main(String[] args) {
        picocli.CommandLine commandLine = new picocli.CommandLine(new Commands()).setUsageHelpWidth(120);

        commandLine.execute(args);
    }

    public static void calculateAverage(HDRImage firstImage, HDRImage secondImage, String outputFileName) throws IOException {
        HDRImage image = new HDRImage(500, 500);
        for(int i = 0; i < image.width; ++i) {
            for (int j = 0; j < image.height; ++j) {
                image.setPixel(i, j, firstImage.getPixel(i, j).sum(secondImage.getPixel(i, j)).prod(0.5f));
            }
        }
        image.writePfm(new FileOutputStream(outputFileName), LITTLE_ENDIAN);
    }

    public static void calculateAverage(HDRImage[] images, String outputFileName) throws Exception {
        int numImages = images.length;

        int width = images[0].width;
        int height = images[0].height;

        HDRImage averageImage = new HDRImage(width, height);

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color ave = new Color(0.f, 0.f, 0.f);
                for (int k = 0; k < numImages; k++) {
                    HDRImage currentImage = images[k];
                    if (currentImage.width != width || currentImage.height != height){
                        throw new Exception("Dimension error!");
                    }
                    Color pixel = currentImage.getPixel(i, j);
                    ave = ave.sum(pixel);
                }
                ave = ave.prod(1.f/numImages);
                averageImage.setPixel(i, j, ave);
            }
        }

        averageImage.writePfm(new FileOutputStream(outputFileName), LITTLE_ENDIAN);
    }


    public static void pfm2image(float factor, float gamma, String inputFile, String outputFile) throws IOException, InvalidPfmFileFormatException {
        Parameters param = new Parameters(factor, gamma, inputFile, outputFile);
        OutputStream out = new FileOutputStream(param.outputFileName);
        InputStream str = new FileInputStream(param.inputFileName);

        HDRImage img = PfmCreator.readPfmImage(str);

        img.normalizeImage(param.factor);
        img.clampImage();
        img.writeLdrImage(out, "PNG", param.gamma);
    }

    public static void pfm2image(float factor, float gamma, String inputFile, String outputFile, float luminosity) throws IOException, InvalidPfmFileFormatException {
        Parameters param = new Parameters(factor, gamma, inputFile, outputFile);
        OutputStream out = new FileOutputStream(param.outputFileName);
        InputStream str = new FileInputStream(param.inputFileName);

        HDRImage img = PfmCreator.readPfmImage(str);

        img.normalizeImage(param.factor, luminosity);
        img.clampImage();
        img.writeLdrImage(out, "PNG", param.gamma);
    }

    public static void demo(Parameters parameters)
            throws InvalidMatrixException, IOException, InvalidPfmFileFormatException {


        Material skyMaterial = new Material(new DiffuseBRDF(new UniformPigment(Black)), new UniformPigment(White));
        Material mirrorMaterial = new Material(new SpecularBRDF(new UniformPigment(DarkOrange)));
        Material DiffuseNavy = new Material(new DiffuseBRDF(new UniformPigment(Navy)));
        Material lightblue = new Material(new DiffuseBRDF(new UniformPigment(SkyBlue)));
        Material brown = new Material(new DiffuseBRDF(new UniformPigment(DarkBrown)));
        Material orange = new Material(new DiffuseBRDF(new UniformPigment(DarkOrange)));
        Material green =  new Material(new DiffuseBRDF(new UniformPigment(Green)));
        Material sphereMaterial2 = new Material(new DiffuseBRDF(new UniformPigment(Yellow)));
        Material black = new Material(new DiffuseBRDF(new UniformPigment(Black)));
        Material gray = new Material(new DiffuseBRDF(new UniformPigment(Gray)));
        Material groundMaterial = new Material(new DiffuseBRDF(new CheckeredPigment(

                                new Color(0.f, 0.5f, 0.f),
                                new Color(1f, 1f, 1f), 1)), new UniformPigment(Black));


        //InputStream str = new FileInputStream("Plank.pfm");
        //HDRImage worldImage = PfmCreator.readPfmImage(str);
        //Material worldSphere = new Material(new DiffuseBRDF(new ImagePigment(worldImage)), new UniformPigment(White));

        Transformation rotation = Transformation.rotationZ(parameters.angleDeg);
        World world = new World();

        /*Material skyMaterial = new Material(
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
        Transformation rescale = Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f));

        for (float i = -0.5f; i <= 0.5f; i += 1.0f) {
            for (float j = -0.5f; j <= 0.5f; j += 1.0f) {
                for (float k = -0.5f; k <= 0.5f; k += 1.0f) {
                    Transformation translation = Transformation.translation(new Vec(i, j, k));
                    world.addShape(new Sphere(translation.times(rescale), sphereMaterial1));
                }
            }
        }

        //SKY
        Transformation rescale = Transformation.scaling(new Vec(50f, 50f, 50f));
        Transformation translation = Transformation.translation(new Vec(0.f, 0.f, 0.f));
        world.addShape(new Sphere(translation.times(rescale), skyMaterial));

        //CUBE
        translation = Transformation.translation(new Vec(0f, 0f, 0.042f));
        world.addShape(new Box(new Point(-0.2f,-0.2f,-0.2f), new Point(0.2f, 0.2f, 0.2f),
                translation.times(Transformation.rotationX(40).times(Transformation.rotationY(45))), DiffuseNavy));

        //PLANE
        world.addShape(new Plain(Transformation.translation(new Vec(0.f, 0.f, -0.2f)), groundMaterial));       //SPHERE 1
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
        world.addShape(sphere2);

        //CUBE
        translation = Transformation.translation(new Vec(0.25f, 0.25f, -0.01f));
        Box cube = new Box(new Point(-0.07f,-0.07f,-0.07f), new Point(0.07f, 0.07f, 0.07f),
                translation.times(Transformation.rotationX(40).times(Transformation.rotationY(45))), hyperboloidmaterial);
        //world.addShape(cube);

        //CSGDifference sphereCube = new CSGDifference(sphere2, cube);
        //world.addShape(sphereCube);

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


        // CYLINDER
        rescale = Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f));
        translation = Transformation.translation(new Vec(-0.5f, 0.15f, -0.05f));
        world.addShape(new Cylinder(translation.times(Transformation.rotationZ(40).times(Transformation.rotationX(90).times(rescale))), DiffuseLime));

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
*/



        //SKY
        Transformation rescale = Transformation.scaling(new Vec(50f, 50f, 50f));
        Transformation translation = Transformation.translation(new Vec(0.f, 0.f, 0.f));
        world.addShape(new Sphere(translation.times(rescale), skyMaterial));

        //CUBE
        /* translation = Transformation.translation(new Vec(0f, 0f, 0.042f));
        world.addShape(new Box(new Point(-0.2f,-0.2f,-0.2f), new Point(0.2f, 0.2f, 0.2f),
                translation.times(Transformation.rotationX(40).times(Transformation.rotationY(45))), DiffuseNavy));

        translation = Transformation.translation(new Vec(0.f, 0.f, -0.1f));
        //PLANE
        world.addShape(new Plain(translation, groundMaterial));
        //SPHERE 1
        rescale = Transformation.scaling(new Vec(0.2f, 0.2f, 0.2f));
        translation = Transformation.translation(new Vec(0.f, 0.5f, 0.1f));
        world.addShape(new Sphere(translation.times(rescale), groundMaterial));
*/
        //SPHERE 2
        rescale = Transformation.scaling(new Vec(0.5f, 0.5f, 0.5f));
        Transformation dilatation = Transformation.scaling(new Vec(0.8f, 0.4f, 0.4f));
        Transformation rot = Transformation.rotationX(90);
        Transformation rot2 = Transformation.rotationZ(180);
        Transformation transl = Transformation.translation(new Vec(-0.25f, 0f, 0.25f));
        Transformation transl2 = Transformation.translation(new Vec(-1.1f, 0.4f, 0f));
        //world.addShape(new Sphere(tra.times(rescale), sphereMaterial2));

        Transformation tran = Transformation.translation(new Vec(1f, -0.5f, -0.f));



        TriangleMesh cat= new TriangleMesh(sphereMaterial2,
                dilatation.times(transl2.times(rot2.times(rot)).times(rescale)));
        TriangleMesh deer= new TriangleMesh(gray,
                transl.times(rot2.times(rot)).times(rescale));
        TriangleMesh wolf= new TriangleMesh(gray);


        cat.createFileShape("cat.txt");
        wolf.createFileShape("wolf.txt");


        //world.addShape(wolf);
        //world.addShape(cat);

        ArrayList<Point> vert = new ArrayList<>();
        vert.add(new Point(-0f, 0f, 0.5f));
        vert.add(new Point(-0f, 0.5f, 0.f));
        vert.add(new Point(-0.f, -0.5f, 0f));
        vert.add(new Point(-0.5f, -0.0f, 0.0f));
        Triangle tri = new Triangle(vert.get(0), vert.get(1),vert.get(2), DiffuseNavy);
        Triangle tri2 = new Triangle(vert.get(0), vert.get(1),vert.get(2), (rot).times(rescale), sphereMaterial2);
        TriangleMesh single= new TriangleMesh(vert, sphereMaterial2);

        //world.addShape(single);
        world.addShape(tri2);

        // MIRROR SPHERE
        //rescale = Transformation.scaling(new Vec(0.25f, 0.2f, 0.2f));
        //translation = Transformation.translation(new Vec(0.2f, -0.5f, 0.1f));
        //world.addShape(new Sphere(translation.times(rescale), mirrorMaterial));

        //PLANE
        //world.addShape(new Plain(Transformation.translation(new Vec(-0.35f, 0, 0)).times(Transformation.rotationY(90)),
          //      brown));
        world.addShape(new Plain(translation, groundMaterial));

        HDRImage image = new HDRImage(parameters.width, parameters.height);
        Camera camera = parameters.orthogonal ?
                new OrthogonalCamera((float) parameters.width/parameters.height, Transformation.translation(new Vec(1.0f, 0.0f, 0.0f))) :
                new PerspectiveCamera(1f, (float) parameters.width/parameters.height, rotation.times(Transformation.translation(new Vec(0.1f, 0f, 0.1f)).times(Transformation.rotationY(3))));

        PCG pcg = new PCG();
        ImageTracer tracer;
        if (parameters.antialiasing){

            tracer = new ImageTracer(image, camera, 4, pcg);
        }else{
            tracer = new ImageTracer(image, camera);
        }
        createPfmImageWithAlgorithm(parameters, world, image, tracer, pcg);

    }

    private static void createPfmImageWithAlgorithm(Parameters parameters, World world, HDRImage image, ImageTracer tracer, PCG pcg) throws InvalidMatrixException, IOException {
        switch (parameters.algorithm) {
            case "flat" -> tracer.fireAllRays(new FlatRenderer(world), parameters.progBarFlushFrequence);
            case "onOff" -> tracer.fireAllRays(new OnOffRenderer(world), parameters.progBarFlushFrequence);
            case "pathTracer" -> {
                if (parameters.parallel) {
                    tracer.fireAllRaysParallel(new PathTracer(world, parameters.numOfRays, parameters.maxDepth, parameters.russianRouletteLimit, pcg), parameters.nThreads, parameters.progBarFlushFrequence);
                } else {
                    tracer.fireAllRays(new PathTracer(world, parameters.numOfRays, parameters.maxDepth, parameters.russianRouletteLimit, pcg), parameters.progBarFlushFrequence);
                }
            }
        }

        image.writePfm(new FileOutputStream(parameters.outputFileName), LITTLE_ENDIAN);
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

    public static void render(Parameters parameters)
            throws InvalidMatrixException, IOException, InvalidPfmFileFormatException, GrammarErrorException {

        InputStream input = new FileInputStream(parameters.inputFileNameTXT);
        InStream inStream = new InStream(input, parameters.inputFileNameTXT);

        Scene scene = inStream.parseScene();
        scene.camera.transformation = Transformation.rotationZ(parameters.angleDeg).times(scene.camera.transformation);

        HDRImage image = new HDRImage(parameters.width, parameters.height);
        ImageTracer tracer;
        PCG pcg = new PCG(parameters.initState, parameters.initSeq);
        if (parameters.antialiasing){
            tracer = new ImageTracer(image, scene.camera, parameters.samplesPerSide, pcg);
        }else{
            tracer = new ImageTracer(image, scene.camera, pcg);
        }

        World world = new World();
        for (int i = 0; i < scene.objects.size();  ++i){
            world.addShape(scene.objects.get(i));
        }
        createPfmImageWithAlgorithm(parameters, world, image, tracer, pcg);
    }
}
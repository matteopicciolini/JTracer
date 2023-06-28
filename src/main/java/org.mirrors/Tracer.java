package org.mirrors;

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
        Material green =  new Material(new DiffuseBRDF(new UniformPigment(Green)));
        Material sphereMaterial2 = new Material(new DiffuseBRDF(new UniformPigment(Yellow)));
        Material groundMaterial = new Material(new DiffuseBRDF(new CheckeredPigment(
                                new Color(0.f, 0.5f, 0.f),
                                new Color(1f, 1f, 1f), 16)), new UniformPigment(Black));

        InputStream str = new FileInputStream("Plank.pfm");
        HDRImage worldImage = PfmCreator.readPfmImage(str);
        Material worldSphere = new Material(new DiffuseBRDF(new ImagePigment(worldImage)), new UniformPigment(White));

        Transformation rotation = Transformation.rotationZ(parameters.angleDeg);
        World world = new World();

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
        rescale = Transformation.scaling(new Vec(0.6f, 0.6f, 0.6f));
        Transformation rot = Transformation.rotationX(40);
        //world.addShape(new Sphere(tra.times(rescale), sphereMaterial2));

        Transformation tran = Transformation.translation(new Vec(-0.5f, 0.1f, 0.f));

        TriangleMesh mesh= new TriangleMesh(green, (rescale).times(tran));
        TriangleMesh mesh2= new TriangleMesh(sphereMaterial2, (tran));
        TriangleMesh tree= new TriangleMesh(brown);
        TriangleMesh tree2= new TriangleMesh(brown);
        TriangleMesh lamp= new TriangleMesh(lightblue, rescale.times(rot).times(tran));
        tree.createFileShape("tree.txt");
        tree2.createFileShape("tree.txt");
        lamp.createFileShape("icosahedron.txt");
        mesh.createFileShape("icosahedron.txt");
        mesh2.createFileShape("icosahedron.txt");

        //world.addShape(mesh);
        world.addShape(lamp);
        //world.addShape(tree);
        //world.addShape(tree2);


        ArrayList<Point> vert = new ArrayList<>();
        vert.add(new Point(-0f, 0f, 0.5f));
        vert.add(new Point(-0f, 0.5f, 0.f));
        vert.add(new Point(-0.f, -0.5f, 0f));
        vert.add(new Point(-0.5f, -0.0f, 0.0f));
        Triangle tri = new Triangle(vert.get(0), vert.get(1),vert.get(2), DiffuseNavy);
        Triangle tri2 = new Triangle(vert.get(0), vert.get(1),vert.get(2), (rot).times(rescale), sphereMaterial2);
        TriangleMesh single= new TriangleMesh(vert, sphereMaterial2, (rescale).times(tran));

        //world.addShape(single);
        //world.addShape(tri2);

        // MIRROR SPHERE
        //rescale = Transformation.scaling(new Vec(0.25f, 0.2f, 0.2f));
        //translation = Transformation.translation(new Vec(0.2f, -0.5f, 0.1f));
        //world.addShape(new Sphere(translation.times(rescale), mirrorMaterial));

        //PLANE
        world.addShape(new Plain(translation, groundMaterial));

        HDRImage image = new HDRImage(parameters.width, parameters.height);
        Camera camera = parameters.orthogonal ?
                new OrthogonalCamera((float) parameters.width/parameters.height, Transformation.translation(new Vec(1.0f, 0.0f, 0.0f))) :
                new PerspectiveCamera(1f, (float) parameters.width/parameters.height, rotation.times(Transformation.translation(new Vec(0.1f, 0f, 0.1f)).times(Transformation.rotationY(3))));

        ImageTracer tracer;
        if (parameters.antialiasing){
            tracer = new ImageTracer(image, camera, 2, new PCG());
        }else{
            tracer = new ImageTracer(image, camera);
        }

        createPfmImageWithAlgorithm(parameters, world, image, tracer);
    }

    private static void createPfmImageWithAlgorithm(Parameters parameters, World world, HDRImage image, ImageTracer tracer) throws InvalidMatrixException, IOException {
        switch (parameters.algorithm) {
            case "flat" -> tracer.fireAllRays(new FlatRenderer(world), parameters.progBarFlushFrequence);
            case "onOff" -> tracer.fireAllRays(new OnOffRenderer(world), parameters.progBarFlushFrequence);
            case "pathTracer" -> {
                if (parameters.parallelAntialiasing) {
                    tracer.fireAllRaysParallel(new PathTracer(world, parameters.numOfRays, parameters.maxDepth, parameters.russianRouletteLimit), parameters.nThreads, parameters.progBarFlushFrequence);
                } else {
                    tracer.fireAllRays(new PathTracer(world, parameters.numOfRays, parameters.maxDepth, parameters.russianRouletteLimit), parameters.progBarFlushFrequence);
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
        if (parameters.antialiasing){
            tracer = new ImageTracer(image, scene.camera, parameters.samplesPerSide, new PCG());
        }else{
            tracer = new ImageTracer(image, scene.camera);
        }

        World world = new World();
        for (int i = 0; i < scene.objects.size();  ++i){
            world.addShape(scene.objects.get(i));
        }
        createPfmImageWithAlgorithm(parameters, world, image, tracer);
    }
}
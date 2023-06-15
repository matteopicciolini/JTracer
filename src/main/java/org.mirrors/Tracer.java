package org.mirrors;

import org.mirrors.compiler.GrammarErrorException;
import org.mirrors.compiler.InStream;
import org.mirrors.compiler.Scene;
import java.io.*;
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
        Material sphereMaterial2 = new Material(new DiffuseBRDF(new UniformPigment(Yellow)));
        Material groundMaterial = new Material(new DiffuseBRDF(new CheckeredPigment(
                                new Color(0.f, 0.5f, 0.f),
                                new Color(1f, 1f, 1f), 16)), new UniformPigment(Black));

        //InputStream str = new FileInputStream("Plank.pfm");
        //HDRImage worldImage = PfmCreator.readPfmImage(str);
        //Material worldSphere = new Material(new DiffuseBRDF(new ImagePigment(worldImage), 1.f));

        Transformation rotation = Transformation.rotationZ(parameters.angleDeg);
        World world = new World();

        //SKY
        Transformation rescale = Transformation.scaling(new Vec(50f, 50f, 50f));
        Transformation translation = Transformation.translation(new Vec(0.f, 0.f, 0.f));
        world.addShape(new Sphere(translation.times(rescale), skyMaterial));

        //CUBE
        translation = Transformation.translation(new Vec(0f, 0f, 0.042f));
        world.addShape(new Box(new Point(-0.2f,-0.2f,-0.2f), new Point(0.2f, 0.2f, 0.2f),
                translation.times(Transformation.rotationX(40).times(Transformation.rotationY(45))), DiffuseNavy));

        translation = Transformation.translation(new Vec(0.f, 0.f, -0.1f));
        //PLANE
        world.addShape(new Plain(translation, groundMaterial));
        //SPHERE 1
        rescale = Transformation.scaling(new Vec(0.2f, 0.2f, 0.2f));
        translation = Transformation.translation(new Vec(0.f, 0.5f, 0.1f));
        world.addShape(new Sphere(translation.times(rescale), groundMaterial));

        //SPHERE 2
        rescale = Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f));
        translation = Transformation.translation(new Vec(0.4f, 0.3f, 0.0f));
        world.addShape(new Sphere(translation.times(rescale), sphereMaterial2));

        // MIRROR SPHERE
        rescale = Transformation.scaling(new Vec(0.25f, 0.2f, 0.2f));
        translation = Transformation.translation(new Vec(0.2f, -0.5f, 0.1f));
        world.addShape(new Sphere(translation.times(rescale), mirrorMaterial));

        HDRImage image = new HDRImage(parameters.width, parameters.height);
        Camera camera = parameters.orthogonal ?
                new OrthogonalCamera((float) parameters.width/parameters.height, Transformation.translation(new Vec(1.0f, 0.0f, 0.0f))) :
                new PerspectiveCamera(1f, (float) parameters.width/parameters.height, rotation.times(Transformation.translation(new Vec(0.1f, 0f, 0.1f)).times(Transformation.rotationY(3))));

        ImageTracer tracer;
        if (parameters.antialiasing){
            tracer = new ImageTracer(image, camera, 4, new PCG());
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
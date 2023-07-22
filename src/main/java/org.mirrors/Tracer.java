package org.mirrors;

import org.mirrors.compiler.GrammarErrorException;
import org.mirrors.compiler.InStream;
import org.mirrors.compiler.Scene;

import java.io.*;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.mirrors.Global.Black;
import static org.mirrors.Global.Yellow;

public class Tracer {
    public static void main(String[] args) {
        picocli.CommandLine commandLine = new picocli.CommandLine(new Commands()).setUsageHelpWidth(120);

        commandLine.execute(args);
    }

    public static void calculateAverage(HDRImage firstImage, HDRImage secondImage, String outputFileName) throws IOException {
        HDRImage image = new HDRImage(500, 500);
        for (int i = 0; i < image.width; ++i) {
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
                    if (currentImage.width != width || currentImage.height != height) {
                        throw new Exception("Dimension error!");
                    }
                    Color pixel = currentImage.getPixel(i, j);
                    ave = ave.sum(pixel);
                }
                ave = ave.prod(1.f / numImages);
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

        Material skyMaterial = new Material(new DiffuseBRDF(new UniformPigment(Black)), new UniformPigment(new Color(0.1f, 0.1f, 0.1f)));
        Material sphereMaterial = new Material(new SpecularBRDF(new UniformPigment(Yellow)));
        Material boxMaterial = new Material(new DiffuseBRDF(new UniformPigment(new Color(1f, 0f, 0f))), new UniformPigment(Black));
        Material groundMaterial = new Material(new DiffuseBRDF(new CheckeredPigment(
                new Color(0.f, 0.5f, 0.f),
                new Color(1f, 1f, 1f), 16)), new UniformPigment(Black)
        );

        Transformation rotation = Transformation.rotationZ(parameters.angleDeg);
        World world = new World();


        //SKY
        Transformation rescale = Transformation.scaling(new Vec(50f, 50f, 50f));
        Transformation translation = Transformation.translation(new Vec(0.f, 0.f, 0.f));
        world.addShape(new Sphere(translation.times(rescale), skyMaterial));

        //CUBE
        translation = Transformation.translation(new Vec(0f, 0f, 0.042f));
        world.addShape(new Box(new Point(-0.2f, -0.2f, -0.2f), new Point(0.2f, 0.2f, 0.2f),
                translation.times(Transformation.rotationX(40).times(Transformation.rotationY(45))), boxMaterial));
        translation = Transformation.translation(new Vec(0.f, 0.f, -0.1f));

        //PLANE
        world.addShape(new Plain(translation, groundMaterial));

        //SPHERE 1
        rescale = Transformation.scaling(new Vec(0.2f, 0.2f, 0.2f));
        translation = Transformation.translation(new Vec(0.f, 0.5f, 0.1f));
        world.addShape(new Sphere(translation.times(rescale), sphereMaterial));


        HDRImage image = new HDRImage(parameters.width, parameters.height);
        Camera camera = parameters.orthogonal ?
                new OrthogonalCamera((float) parameters.width / parameters.height, Transformation.translation(new Vec(1.0f, 0.0f, 0.0f))) :
                new PerspectiveCamera(1f, (float) parameters.width / parameters.height, rotation.times(Transformation.translation(new Vec(0.1f, 0f, 0.1f)).times(Transformation.rotationY(3))));

        PCG pcg = new PCG();
        ImageTracer tracer;
        if (parameters.antialiasing) {
            tracer = new ImageTracer(image, camera, 4, pcg);
        } else {
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
                System.out.println("Unable to delete the file " + fileName + ".");
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
        if (parameters.antialiasing) {
            tracer = new ImageTracer(image, scene.camera, parameters.samplesPerSide, pcg);
        } else {
            tracer = new ImageTracer(image, scene.camera, pcg);
        }

        World world = new World();
        for (int i = 0; i < scene.objects.size(); ++i) {
            world.addShape(scene.objects.get(i));
        }
        createPfmImageWithAlgorithm(parameters, world, image, tracer, pcg);
    }
}
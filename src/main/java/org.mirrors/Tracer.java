package org.mirrors;

import org.mirrors.compiler.GrammarError;
import org.mirrors.compiler.InStream;
import org.mirrors.compiler.Scene;
import picocli.CommandLine;

import java.io.*;

import static java.lang.Integer.parseInt;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.mirrors.Global.*;

import java.io.File;

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

    public static void demo(int width, int height, float angleDeg, boolean orthogonal,
                            String fileOutputPFM, String algorithm, boolean antialiasing,
                            boolean parallelAntialiasing, int nThreads)
            throws InvalidMatrixException, IOException, InvalidPfmFileFormatException {
        long time = System.currentTimeMillis();


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

        Transformation rotation = Transformation.rotationZ(angleDeg);
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

        HDRImage image = new HDRImage(width, height);
        Camera camera = orthogonal ?
                new OrthogonalCamera((float) width/height, Transformation.translation(new Vec(1.0f, 0.0f, 0.0f))) :
                new PerspectiveCamera(1f, (float) width/height, rotation.times(Transformation.translation(new Vec(0.1f, 0f, 0.1f)).times(Transformation.rotationY(3))));

        ImageTracer tracer;
        if (antialiasing){
            tracer = new ImageTracer(image, camera, 4, new PCG());
        }else{
            tracer = new ImageTracer(image, camera);
        }

        switch (algorithm) {
            case "flat" -> tracer.fireAllRays(new FlatRenderer(world));
            case "onOff" -> tracer.fireAllRays(new OnOffRenderer(world));
            case "pathTracer" -> {
                if (parallelAntialiasing) {
                    tracer.fireAllRaysParallel(new PathTracer(world), nThreads);
                } else {
                    tracer.fireAllRays(new PathTracer(world));
                }
            }
        }

        image.writePfm(new FileOutputStream(fileOutputPFM), LITTLE_ENDIAN);
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

    public static void render(int width, int height, float angleDeg, boolean orthogonal,
                            String fileOutputPFM, String algorithm, boolean antialiasing,
                            boolean parallelAntialiasing, int nThreads)
            throws InvalidMatrixException, IOException, InvalidPfmFileFormatException, GrammarError {
        long time = System.currentTimeMillis();
        //-------------------------------------------------
        /*Material skyMaterial = new Material(new DiffuseBRDF(new UniformPigment(Black)), new UniformPigment(White));
        Material groundMaterial = new Material(new DiffuseBRDF(new CheckeredPigment(
                new Color(0.f, 0.5f, 0.f),
                new Color(1f, 1f, 1f), 16)), new UniformPigment(Black));


        Transformation rotation = Transformation.rotationZ(angleDeg);
        World world = new World();

        //SKY
        Transformation rescale = Transformation.scaling(new Vec(50f, 50f, 50f));
        Transformation translation = Transformation.translation(new Vec(0.f, 0.f, 0.f));
        world.addShape(new Sphere(translation.times(rescale), skyMaterial));

        //PLANE
        world.addShape(new Plain(Transformation.translation(new Vec(0.f, 0.f, -0.1f)), groundMaterial));
        //SPHERE 1
        rescale = Transformation.scaling(new Vec(0.2f, 0.2f, 0.2f));
        translation = Transformation.translation(new Vec(0.f, 0.5f, 0.1f));
        world.addShape(new Sphere(translation.times(rescale), groundMaterial));

        HDRImage image = new HDRImage(width, height);
        Camera camera = orthogonal ?
                new OrthogonalCamera((float) width/height, Transformation.translation(new Vec(1.0f, 0.0f, 0.0f))) :
                new PerspectiveCamera(1f, (float) width/height, rotation.times(Transformation.translation(new Vec(0.1f, 0f, 0.1f)).times(Transformation.rotationY(3))));

        ImageTracer tracer;
        if (antialiasing){
            tracer = new ImageTracer(image, camera, 4, new PCG());
        }else{
            tracer = new ImageTracer(image, camera);
        }*/
        //-------------------------------------------------
        InputStream input = new FileInputStream("firstImage.txt");

        InStream inStream = new InStream(input);
        Scene scene = inStream.parseScene();

        //Transformation rotation = Transformation.rotationZ(angleDeg);
        HDRImage image = new HDRImage(width, height);
        ImageTracer tracer;
        if (antialiasing){
            tracer = new ImageTracer(image, scene.camera, 4, new PCG());
        }else{
            tracer = new ImageTracer(image, scene.camera);
        }

        World world = new World();
        for (int i = 0; i < scene.objects.size();  ++i){
            world.addShape(scene.objects.get(i));
        }
        switch (algorithm) {
            case "flat" -> tracer.fireAllRays(new FlatRenderer(world));
            case "onOff" -> tracer.fireAllRays(new OnOffRenderer(world));
            case "pathTracer" -> {
                if (parallelAntialiasing) {
                    tracer.fireAllRaysParallel(new PathTracer(world), nThreads);
                } else {
                    tracer.fireAllRays(new PathTracer(world));
                }
            }
        }

        image.writePfm(new FileOutputStream(fileOutputPFM), LITTLE_ENDIAN);
        long time2 = System.currentTimeMillis();
        System.out.println(time2 - time);
    }
}
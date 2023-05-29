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

                if (dArgs != null) {
                    if (dArgs.length >= 1 && dArgs.length <= 6) width = parseInt(dArgs[0]);
                    if (dArgs.length >= 2 && dArgs.length <= 6) height = parseInt(dArgs[1]);
                    if (dArgs.length >= 3 && dArgs.length <= 6) angleDeg = parseInt(dArgs[2]);
                    if (dArgs.length >= 4 && dArgs.length <= 6) orthogonal = parseBoolean(dArgs[3]);
                    if (dArgs.length >= 5 && dArgs.length <= 6) fileOutput = dArgs[4];
                    if (dArgs.length == 6) algorithm = dArgs[5];
                    if (dArgs.length > 6) {
                        System.err.println("Error: ");
                        formatter.printHelp("Tracer", options);
                    }
                }
                demo(width, height, angleDeg, orthogonal, fileOutput, algorithm);
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

        img.normalizeImage(param.factor);
        //img.normalizeImage(param.factor, 0.5f);
        img.clampImage();
        img.writeLdrImage(out, "PNG", param.gamma);
    }

    public static void demo(int width, int height, float angleDeg, boolean orthogonal, String fileOutputPFM, String algorithm) throws InvalidMatrixException, IOException, InvalidPfmFileFormatException {
        long time = System.currentTimeMillis();


        Material skyMaterial = new Material(
                new DiffuseBRDF(new UniformPigment(new Color())),
                new UniformPigment(White)
        );
        Material mirrorMaterial = new Material(new SpecularBRDF(new UniformPigment(DarkOrange)));
        Material sphereMaterial1 = new Material(new DiffuseBRDF(new UniformPigment(new Color(0.3f, 0.4f, 0.8f))));
        Material groundMaterial = new Material(
                new DiffuseBRDF(
                        new CheckeredPigment(
                                new Color(0.3f, 0.5f, 0.1f),
                                new Color(0.1f, 0.2f, 0.5f), 16
                        )
                )
        );
        /*Material checkeredMaterial = new Material(
                new DiffuseBRDF(
                        new CheckeredPigment(
                                new Color(1f, 0.5f, 0.5f),
                                new Color(1f, 0.2f, 0.2f),15
                        ), 1.f
                )
        );*/

        //InputStream str = new FileInputStream("Plank.pfm");
        //HDRImage worldImage = PfmCreator.readPfmImage(str);
        //Material worldSphere = new Material(new DiffuseBRDF(new UniformPigment(new Color (1, 1, 1)), 1.f));
        Transformation rotation = Transformation.rotationZ(angleDeg);
        Transformation rescale = Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f));
        World world = new World();
        /*for (float i = -0.5f; i <= 0.5f; i += 1.0f) {
            for (float j = -0.5f; j <= 0.5f; j += 1.0f) {
                for (float k = -0.5f; k <= 0.5f; k += 1.0f) {
                    Transformation translation = Transformation.translation(new Vec(i, j, k));
                    world.addShape(new Sphere(rotation.times(translation.times(rescale)), sphereMaterial1));
                }
            }
        }*/
        Transformation translation = Transformation.translation(new Vec(0f, 0f, -0.25f));
        rescale = Transformation.scaling(new Vec(0.25f, 0.25f, 0.25f));
        world.addShape(new Sphere(rotation.times(translation.times(rescale)), sphereMaterial1));

        //Transformation translation = Transformation.translation(new Vec(0.f, 0.f, 0f));f
        //world.addShape(new Sphere(rotation.times(translation.times(rescale)), worldSphere));


        rescale = Transformation.scaling(new Vec(50f, 50f, 50f));
        translation = Transformation.translation(new Vec(0.f, 0.f, 0.f));
        world.addShape(new Sphere(rotation.times(translation.times(rescale)), skyMaterial));
        world.addShape(new Plain(Transformation.translation(new Vec(0.f, 0.f, -0.5f)), groundMaterial));

        rescale = Transformation.scaling(new Vec(0.2f, 0.2f, 0.2f));
        translation = Transformation.translation(new Vec(-0.1f, 0.5f, -0.3f));
        world.addShape(new Sphere(rotation.times(translation.times(rescale)), mirrorMaterial));


        HDRImage image = new HDRImage(width, height);
        Camera camera = orthogonal ?
                new OrthogonalCamera((float) width/height, Transformation.translation(new Vec(1.0f, 0.0f, 0.0f))) :
                new PerspectiveCamera(1f, (float) width/height, Transformation.translation(new Vec(0.1f, 0.0f, 0.0f)).times(Transformation.rotationY(3)));

        ImageTracer tracer = new ImageTracer(image, camera, 4, new PCG());
        if(algorithm.equals("flat")){
            tracer.fireAllRays(new FlatRenderer(world));
        }
        else if (algorithm.equals("onOff")){
            tracer.fireAllRays(new OnOffRenderer(world));
        }
        else if (algorithm.equals("pathTracer")){
            tracer.fireAllRaysParallel(new PathTracer(world), 2);
        }

        image.writePfm(new FileOutputStream(fileOutputPFM), LITTLE_ENDIAN);
        String fileOutputPNG = fileOutputPFM.substring(0, fileOutputPFM.length() - 3) + "png";
        pfm2image(0.18f, 2.2f, fileOutputPFM, fileOutputPNG);
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
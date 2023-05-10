package org.mirrors;
import org.apache.commons.cli.*;
import java.io.*;

import static java.lang.Boolean.parseBoolean;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

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
                .argName("width> <height> <angle-deg> <orthogonal")
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

            if(cmd.hasOption("d")){
                String[] dArgs = cmd.getOptionValues("d");

                int width = 1920;
                int height = 1080;
                float angleDeg = 0.f;
                boolean orthogonal = false;

                if (dArgs != null) {
                    if (dArgs.length >= 1 && dArgs.length <= 4) width = parseInt(dArgs[0]);
                    if (dArgs.length >= 2 && dArgs.length <= 4) height = parseInt(dArgs[1]);
                    if (dArgs.length >= 3 && dArgs.length <= 4) angleDeg = parseInt(dArgs[2]);
                    if (dArgs.length == 4) orthogonal = parseBoolean(dArgs[3]);
                    if (dArgs.length > 4) {
                        System.err.println("Error: ");
                        formatter.printHelp("Tracer", options);
                    }
                }
                demo(width, height, angleDeg, orthogonal);
            }
        }
        catch (ParseException e) {
            System.err.println("Error: " + e.getMessage());
            formatter.printHelp("Tracer", options);
            throw new RuntimeException(e);
        } catch (IOException | InvalidPfmFileFormatException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e){
            formatter.printHelp("Tracer", options);
            throw new RuntimeException(e);
        } catch (InvalidMatrixException e) {
            throw new RuntimeException(e);
        }
    }

    private static void pfm2image(float factor, float gamma, String inputFile, String outputFile) throws IOException, InvalidPfmFileFormatException {
        Parameters param = new Parameters(factor, gamma, inputFile, outputFile);
        OutputStream out = new FileOutputStream(param.outputFileName);
        InputStream str = new FileInputStream(param.inputFileName);

        HDRImage img = PfmCreator.readPfmImage(str);

        img.normalizeImage(param.factor);
        img.clampImage();
        img.writeLdrImage(out, "PNG", param.gamma);
    }

    private static void demo(int width, int height, float angleDeg, boolean orthogonal) throws InvalidMatrixException, IOException, InvalidPfmFileFormatException {
        World world = new World();
        for (float i = -0.5f; i <= 0.5f; i += 1.0f) {
            for (float j = -0.5f; j <= 0.5f; j += 1.0f) {
                for (float k = -0.5f; k <= 0.5f; k += 1.0f) {
                    world.addShape(new Sphere(Transformation.translation(new Vec(i, j, k)).times(Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f)))));
                }
            }
        }
        world.addShape(new Sphere(Transformation.translation(new Vec(0.f, 0.f, -0.5f)).times(Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f)))));
        world.addShape(new Sphere(Transformation.translation(new Vec(0.f, 0.5f, 0.f)).times(Transformation.scaling(new Vec(0.1f, 0.1f, 0.1f)))));


        HDRImage image = new HDRImage(width, height);
        Camera camera = orthogonal == true ?
                new OrthogonalCamera(16.f / 9.f, Transformation.translation(new Vec(-1.0f, -0.0f, 0.0f))) :
                new PerspectiveCamera(1.f, 16.f / 9.f, Transformation.translation(new Vec(-1.0f, -0.0f, 0.0f)));
        ImageTracer tracer = new ImageTracer(image, camera);
        tracer.fireAllRays(
                ray -> {
            try {
                return world.rayIntersection(ray) != null ? new Color(1.f, 1.f, 1.f) : new Color(0f, 0.f, 0.f);
            }
            catch (InvalidMatrixException e) {
                throw new RuntimeException(e);
            }
        });

        image.writePfm(new FileOutputStream("image.pfm"), LITTLE_ENDIAN);
        pfm2image(0.18f, 2.2f, "image.pfm", "image.png");
    }
}


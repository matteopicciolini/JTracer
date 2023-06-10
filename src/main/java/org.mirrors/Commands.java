package org.mirrors;
import org.mirrors.compiler.GrammarErrorException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.util.Objects;

@Command(name = "", description = ".NET Core console app with argument parsing.", mixinStandardHelpOptions = true)
public class Commands implements Runnable{
    public static Float valueOfLuminosity(String algorithm){
        if(!Objects.equals(algorithm, "pathTracer")) return 0.5f;
        else return null;
    }
    @Command(name = "demo", description = "Demo of JTracer.", mixinStandardHelpOptions = true)
    public void demo(
            @Option(names = {"-w", "--width"}, description = "int: Width of the image. Default: ${DEFAULT-VALUE}.", defaultValue = "480") Integer width,
            @Option(names = {"--height"}, description = "int: Height of the image. Default: ${DEFAULT-VALUE}.", defaultValue = "480") Integer height,
            @Option(names = {"-a", "--angle-deg"}, description = "float: Angle of view. Default: ${DEFAULT-VALUE}.", defaultValue = "0") Float angleDeg,
            @Option(names = {"--output"}, description = "string: Path of the output ldr file. Default: ${DEFAULT-VALUE}.", defaultValue = "img.pfm") String outputFilename,
            @Option(names = {"--algorithm"}, description = "string: Algorithm of rendering. Default: ${DEFAULT-VALUE}.", defaultValue = "pathTracer") String algorithm,
            @Option(names = {"-o", "--orthogonal"}, description = "bool: Use an orthogonal camera. Default: ${DEFAULT-VALUE}.", defaultValue = "false") Boolean orthogonal,
            @Option(names = {"--antialiasing"}, description = "bool: Use antialiasing algorithm. Default: ${DEFAULT-VALUE}.", defaultValue = "false") Boolean antialiasing,
            @Option(names = {"--parallelAntialiasing"}, description = "bool: Parallelize antialiasing algorithm. Default: ${DEFAULT-VALUE}.", defaultValue = "false") Boolean parallelAntialiasing,
            @Option(names = {"--nThreads"}, description = "int: Number of threads to use for parallelization. Default: ${DEFAULT-VALUE}.", defaultValue = "4") Integer nThreads,
            @Option(names = {"-c", "--convertToPNG"}, description = "bool: At the end of the program execution, automatically convert the PFM file to PNG. Default: ${DEFAULT-VALUE}.", defaultValue = "true") Boolean convertInPNG,
            @Option(names = {"-d", "--deletePFM"}, description = "bool: At the end of the program execution, keep only the LDR image, deleting the PFM. Default: ${DEFAULT-VALUE}.", defaultValue = "false") Boolean deletePFM,
            //Screen features
            @Option(names = {"-g", "--gamma"}, description = "float: Exponent for gamma-correction. Default: ${DEFAULT-VALUE}.", defaultValue = "2.2") Float gamma,
            @Option(names = {"-f", "--factor"}, description = "float: Multiplicative factor. Default: ${DEFAULT-VALUE}.", defaultValue = "0.18") Float factor,
            @Option(names = {"-l", "--luminosity"}, description = "float: Luminosity of the image. \t Default: It is calculated for the pathTracer; otherwise, it is set to 0.5.") Float luminosity
    ) throws InvalidOptionException {
        if(!convertInPNG && deletePFM){
            throw new InvalidOptionException("If the deletePFM parameter is true, the convertInPNG parameter cannot be false.");
        }
        luminosity = valueOfLuminosity(algorithm);

        try {
            Tracer.demo(width, height, angleDeg, orthogonal, outputFilename, algorithm, antialiasing, parallelAntialiasing, nThreads);

            if(convertInPNG) {
                String fileOutputPNG = outputFilename.substring(0, outputFilename.length() - 3) + "png";
                if (luminosity == null) Tracer.pfm2image(factor, gamma, outputFilename, fileOutputPNG);
                else Tracer.pfm2image(factor, gamma, outputFilename, fileOutputPNG, luminosity);
                if(deletePFM) Tracer.RemoveFile(outputFilename);
            }

        } catch (Exception | InvalidMatrixException | InvalidPfmFileFormatException ex) {
            System.err.println(ex.getMessage());
        }
    }


    @Command(name = "convert", description = "Convert pfm file to png.", mixinStandardHelpOptions = true)
    public void convert(
            @Parameters(index = "0", paramLabel = "-i", description = "String: Path of the input file") String inputFilename,
            @Option(names = {"-o", "--outputFileName"}, description = "String: Path of the output ldr file") String outputFilename,
            @Option(names = {"-g", "--gamma"}, description = "float: Exponent for gamma-correction") Float gamma,
            @Option(names = {"-f", "--factor"}, description = "float: Multiplicative factor") Float factor)
    {
        if (outputFilename == null) outputFilename = inputFilename.substring(0, inputFilename.length() - 3) + "png";
        if (gamma == null) gamma = 2.2f;
        if (factor == null) factor = 0.18f;

        try {
            Tracer.pfm2image(factor, gamma, inputFilename, outputFilename);
        } catch (Exception | InvalidPfmFileFormatException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Command(name = "render", description = "Demo of JTracer.", mixinStandardHelpOptions = true)
    public void render(
            @Option(names = {"-w", "--width"}, description = "int: Width of the image. Default: ${DEFAULT-VALUE}.", defaultValue = "480") Integer width,
            @Option(names = {"--height"}, description = "int: Height of the image. Default: ${DEFAULT-VALUE}.", defaultValue = "480") Integer height,
            @Option(names = {"-a", "--angle-deg"}, description = "float: Angle of view. Default: ${DEFAULT-VALUE}.", defaultValue = "0") Float angleDeg,
            @Option(names = {"--output"}, description = "string: Path of the output ldr file. Default: ${DEFAULT-VALUE}.", defaultValue = "img.pfm") String outputFilename,
            @Option(names = {"--algorithm"}, description = "string: Algorithm of rendering. Default: ${DEFAULT-VALUE}.", defaultValue = "pathTracer") String algorithm,
            @Option(names = {"-o", "--orthogonal"}, description = "bool: Use an orthogonal camera. Default: ${DEFAULT-VALUE}.", defaultValue = "false") Boolean orthogonal,
            @Option(names = {"--antialiasing"}, description = "bool: Use antialiasing algorithm. Default: ${DEFAULT-VALUE}.", defaultValue = "false") Boolean antialiasing,
            @Option(names = {"--parallelAntialiasing"}, description = "bool: Parallelize antialiasing algorithm. Default: ${DEFAULT-VALUE}.", defaultValue = "false") Boolean parallelAntialiasing,
            @Option(names = {"--nThreads"}, description = "int: Number of threads to use for parallelization. Default: ${DEFAULT-VALUE}.", defaultValue = "4") Integer nThreads,
            @Option(names = {"-c", "--convertToPNG"}, description = "bool: At the end of the program execution, automatically convert the PFM file to PNG. Default: ${DEFAULT-VALUE}.", defaultValue = "true") Boolean convertInPNG,
            @Option(names = {"-d", "--deletePFM"}, description = "bool: At the end of the program execution, keep only the LDR image, deleting the PFM. Default: ${DEFAULT-VALUE}.", defaultValue = "false") Boolean deletePFM,
            //Screen features
            @Option(names = {"-g", "--gamma"}, description = "float: Exponent for gamma-correction. Default: ${DEFAULT-VALUE}.", defaultValue = "2.2") Float gamma,
            @Option(names = {"-f", "--factor"}, description = "float: Multiplicative factor. Default: ${DEFAULT-VALUE}.", defaultValue = "0.18") Float factor,
            @Option(names = {"-l", "--luminosity"}, description = "float: Luminosity of the image. \t Default: It is calculated for the pathTracer; otherwise, it is set to 0.5.") Float luminosity
    ) throws InvalidOptionException{
        if(!convertInPNG && deletePFM){
            throw new InvalidOptionException("If the deletePFM parameter is true, the convertInPNG parameter cannot be false.");
        }
        luminosity = valueOfLuminosity(algorithm);

        try {
            Tracer.render(width, height, angleDeg, orthogonal, outputFilename, algorithm, antialiasing, parallelAntialiasing, nThreads);

            if(convertInPNG) {
                String fileOutputPNG = outputFilename.substring(0, outputFilename.length() - 3) + "png";
                if (luminosity == null){ Tracer.pfm2image(factor, gamma, outputFilename, fileOutputPNG);}
                else Tracer.pfm2image(factor, gamma, outputFilename, fileOutputPNG, luminosity);
                if(deletePFM) Tracer.RemoveFile(outputFilename);
            }

        }catch (InvalidMatrixException | IOException | GrammarErrorException | InvalidPfmFileFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }
}
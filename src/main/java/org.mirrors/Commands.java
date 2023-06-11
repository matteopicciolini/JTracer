package org.mirrors;
import org.mirrors.compiler.GrammarErrorException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Command(name = "", description = ".NET Core console app with argument parsing.", mixinStandardHelpOptions = true)
public class Commands implements Runnable{
    public static Float valueOfLuminosity(String algorithm){
        if(!Objects.equals(algorithm, "pathTracer")) return 0.5f;
        else return null;
    }
    @Command(name = "demo", description = "JTracer demo.", mixinStandardHelpOptions = true)
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
            //ProgressBar
            @Option(names = {"--flushFrequence"}, description = "int: Frequency of flush (expressed in number of processed pixels) of the progress bar. Default: ${DEFAULT-VALUE}", defaultValue = "100") Integer progBarFlushFrequence,
            //Screen features
            @Option(names = {"-g", "--gamma"}, description = "float: Exponent for gamma-correction. Default: ${DEFAULT-VALUE}.", defaultValue = "2.2") Float gamma,
            @Option(names = {"-f", "--factor"}, description = "float: Multiplicative factor. Default: ${DEFAULT-VALUE}.", defaultValue = "0.18") Float factor,
            @Option(names = {"-l", "--luminosity"}, description = "float: Luminosity of the image. \t Default: It is calculated for the pathTracer; otherwise, it is set to 0.5.") Float luminosity,
            //pathTracer
            @Option(names = {"-n", "--numRays"}, description = "int: Number of rays per pixel", defaultValue = "10") Integer numOfRays,
            @Option(names = {"--maxDepth"}, description = "int: Maximum recursion depth", defaultValue = "2") Integer maxDepth,
            @Option(names = {"--russianRouletteLimit"}, description = "int: Russian roulette limit. Default: ${DEFAULT-VALUE}.", defaultValue = "3") Integer russianRouletteLimit
    ) throws InvalidOptionException {
        if(!convertInPNG && deletePFM){
            throw new InvalidOptionException("If the deletePFM parameter is true, the convertInPNG parameter cannot be false.");
        }
        luminosity = valueOfLuminosity(algorithm);

        org.mirrors.Parameters parameters = new org.mirrors.Parameters(width, height, angleDeg,
                outputFilename, orthogonal, algorithm, antialiasing, parallelAntialiasing, nThreads,
                convertInPNG, deletePFM, progBarFlushFrequence, gamma, factor, luminosity, numOfRays,
                maxDepth, russianRouletteLimit);

        try {
            Tracer.demo(parameters);
            conversion(outputFilename, convertInPNG, deletePFM, gamma, factor, luminosity);
        } catch (Exception | InvalidMatrixException | InvalidPfmFileFormatException e) {
            throw new RuntimeException(e);
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
        } catch (Exception | InvalidPfmFileFormatException e) {
            throw new RuntimeException(e);
        }
    }

    @Command(name = "render", description = "JTracer render.", mixinStandardHelpOptions = true)
    public void render(
            @Option(names = {"-i", "--input"}, required = true,description = "string: Path of the input TXT file. REQUIRED.") String inputFileNameTXT,
            @Option(names = {"-w", "--width"}, description = "int: Width of the image. Default: ${DEFAULT-VALUE}.", defaultValue = "480") Integer width,
            @Option(names = {"--height"}, description = "int: Height of the image. Default: ${DEFAULT-VALUE}.", defaultValue = "480") Integer height,
            @Option(names = {"-a", "--angle-deg"}, description = "float: Angle of view. Default: ${DEFAULT-VALUE}.", defaultValue = "0") Float angleDeg,
            @Option(names = {"--output"}, description = "string: Path of the output ldr file. Default: ${DEFAULT-VALUE}.", defaultValue = "img.pfm") String outputFileName,
            @Option(names = {"--algorithm"}, description = "string: Algorithm of rendering. Default: ${DEFAULT-VALUE}.", defaultValue = "pathTracer") String algorithm,
            //@Option(names = {"-o", "--orthogonal"}, description = "bool: Use an orthogonal camera. Default: ${DEFAULT-VALUE}.", defaultValue = "false") Boolean orthogonal,
            @Option(names = {"--antialiasing"}, description = "bool: Use antialiasing algorithm. Default: ${DEFAULT-VALUE}.", defaultValue = "false") Boolean antialiasing,
            @Option(names = {"--parallelAntialiasing"}, description = "bool: Parallelize antialiasing algorithm. Default: ${DEFAULT-VALUE}.", defaultValue = "true") Boolean parallelAntialiasing,
            @Option(names = {"--nThreads"}, description = "int: Number of threads to use for parallelization. Default: ${DEFAULT-VALUE}.", defaultValue = "4") Integer nThreads,
            @Option(names = {"-c", "--convertToPNG"}, description = "bool: At the end of the program execution, automatically convert the PFM file to PNG. Default: ${DEFAULT-VALUE}.", defaultValue = "true") Boolean convertInPNG,
            @Option(names = {"-d", "--deletePFM"}, description = "bool: At the end of the program execution, keep only the LDR image, deleting the PFM. Default: ${DEFAULT-VALUE}.", defaultValue = "false") Boolean deletePFM,
            //ProgressBar
            @Option(names = {"--flushFrequence"}, description = "int: Frequency of flush (expressed in number of processed pixels) of the progress bar. Default: ${DEFAULT-VALUE}", defaultValue = "100") Integer progBarFlushFrequence,
            //Screen features
            @Option(names = {"-g", "--gamma"}, description = "float: Exponent for gamma-correction. Default: ${DEFAULT-VALUE}.", defaultValue = "2.2") Float gamma,
            @Option(names = {"-f", "--factor"}, description = "float: Multiplicative factor. Default: ${DEFAULT-VALUE}.", defaultValue = "0.18") Float factor,
            @Option(names = {"-l", "--luminosity"}, description = "float: Luminosity of the image. \t Default: It is calculated for the pathTracer; otherwise, it is set to 0.5.") Float luminosity,
            //pathTracer
            @Option(names = {"-n", "--numRays"}, description = "int: Number of rays per pixel", defaultValue = "10") Integer numOfRays,
            @Option(names = {"--maxDepth"}, description = "int: Maximum recursion depth", defaultValue = "2") Integer maxDepth,
            @Option(names = {"--russianRouletteLimit"}, description = "int: Russian roulette limit. Default: ${DEFAULT-VALUE}.", defaultValue = "3") Integer russianRouletteLimit
    ) throws InvalidOptionException{
        if(!convertInPNG && deletePFM){
            throw new InvalidOptionException("If the deletePFM parameter is true, the convertInPNG parameter cannot be false.");
        }
        luminosity = valueOfLuminosity(algorithm);


        org.mirrors.Parameters parameters = new org.mirrors.Parameters(inputFileNameTXT, width, height, angleDeg,
                outputFileName, algorithm, antialiasing, parallelAntialiasing, nThreads, convertInPNG,
                deletePFM, progBarFlushFrequence, gamma, factor, luminosity, numOfRays, maxDepth, russianRouletteLimit);

        try {
            Tracer.render(parameters);
            conversion(outputFileName, convertInPNG, deletePFM, gamma, factor, luminosity);
        }catch (InvalidMatrixException | IOException | GrammarErrorException | InvalidPfmFileFormatException e) {
            throw new RuntimeException(e);
        }
    }

    private void conversion(@Option(names = {"--output"}, description = "string: Path of the output ldr file. Default: ${DEFAULT-VALUE}.", defaultValue = "img.pfm") String outputFilename, @Option(names = {"-c", "--convertToPNG"}, description = "bool: At the end of the program execution, automatically convert the PFM file to PNG. Default: ${DEFAULT-VALUE}.", defaultValue = "true") Boolean convertInPNG, @Option(names = {"-d", "--deletePFM"}, description = "bool: At the end of the program execution, keep only the LDR image, deleting the PFM. Default: ${DEFAULT-VALUE}.", defaultValue = "false") Boolean deletePFM, @Option(names = {"-g", "--gamma"}, description = "float: Exponent for gamma-correction. Default: ${DEFAULT-VALUE}.", defaultValue = "2.2") Float gamma, @Option(names = {"-f", "--factor"}, description = "float: Multiplicative factor. Default: ${DEFAULT-VALUE}.", defaultValue = "0.18") Float factor, @Option(names = {"-l", "--luminosity"}, description = "float: Luminosity of the image. \t Default: It is calculated for the pathTracer; otherwise, it is set to 0.5.") Float luminosity) throws IOException, InvalidPfmFileFormatException {
        if(convertInPNG){
            String fileOutputPNG = outputFilename.substring(0, outputFilename.length() - 3) + "png";
            if (luminosity == null){ Tracer.pfm2image(factor, gamma, outputFilename, fileOutputPNG);}
            else Tracer.pfm2image(factor, gamma, outputFilename, fileOutputPNG, luminosity);
            if(deletePFM) Tracer.RemoveFile(outputFilename);
        }
    }

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }
}
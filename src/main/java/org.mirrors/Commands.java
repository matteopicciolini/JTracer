package org.mirrors;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "dotnet run", description = ".NET Core console app with argument parsing.")
public class Commands implements Runnable{
    @Command(name = "demo", description = "Demo of JTracer.")
    public void demo(
            @Option(names = {"-w", "--width"}, description = "int: Width of the image. \t\t Default: 480") Integer width,
            @Option(names = {"-h", "--height"}, description = "int: Height of the image. \t\t Default: 480") Integer height,
            @Option(names = {"-a", "--angle-deg"}, description = "int: Angle of view. \t\t\t Default: 0") Float angleDeg,
            @Option(names = {"-out", "--output"}, description = "string: Path of the output ldr file. \t Default: img.pfm") String outputFilename,
            @Option(names = {"-al", "--algorithm"}, description = "string: Algorithm of rendering. \t Default: pathTracer") String algorithm,
            @Option(names = {"-o", "--orthogonal"}, description = "bool: Use an orthogonal camera \t Default: false") Boolean orthogonal,
            @Option(names = {"-aa", "--antialiasing"}, description = "bool: Use antialiasing algorithm \t Default: true") Boolean antialiasing,
            @Option(names = {"-paa", "--parallelAntialiasing"}, description = "bool: Parallelize antialiasing algorithm \t Default: false") Boolean parallelAntialiasing,
            @Option(names = {"-nt","--nThreads"}, description = "int: Number of threads to use for parallelization \t Default: 4") Integer nThreads,
            @Option(names = {"-c","--convertToPNG"}, description = "bool: At the end of the program execution, automatically convert the PFM file to PNG.   \t Default: true") Boolean convertInPNG,
            @Option(names = {"-d","--deletePFM"}, description = "bool: At the end of the program execution, keep only the LDR image, deleting the PFM. \t Default: false") Boolean deletePFM,
            //Screen features
            @Option(names = {"-g", "--gamma"}, description = "float: Exponent for gamma-correction. \t Default: 2.2") Float gamma,
            @Option(names = {"-f", "--factor"}, description = "float: Multiplicative factor. \t\t Default: 0.18") Float factor,
            @Option(names = {"-l", "--luminosity"}, description = "float: Luminosity of the image. \t Default: It is calculated for the path tracer; otherwise, it is set to 0.5.") Float luminosity
    ) throws InvalidOptionException {
        if (width == null) width = 480;
        if (height == null) height = 480;
        if (angleDeg == null) angleDeg = 0.f;
        if (orthogonal == null) orthogonal = false;
        if (outputFilename == null) outputFilename = "img.pfm";
        if (algorithm == null) algorithm = "pathTracer";
        if (convertInPNG == null) convertInPNG = true;
        if (deletePFM == null) deletePFM = false;
        if(!convertInPNG && deletePFM){
            throw new InvalidOptionException("If the deletePFM parameter is true, the convertInPNG parameter cannot be false.");
        }

        // Antialiasing and parallelization
        if (antialiasing == null) antialiasing = false;
        if (parallelAntialiasing == null) parallelAntialiasing = false;
        if (nThreads == null) nThreads = 4;

        // Screen features
        if (gamma == null) gamma = 2.2f;
        if (factor == null) factor = 0.18f;
        if (luminosity == null){
            if(!algorithm.equals("pathTracer")){
                luminosity = 0.5f;
            }
        }

        try {
            Tracer.demo(width, height, angleDeg, orthogonal, outputFilename, algorithm, antialiasing, parallelAntialiasing, nThreads);
            if(convertInPNG) {
                String fileOutputPNG = outputFilename.substring(0, outputFilename.length() - 3) + "png";
                Tracer.pfm2image(factor, gamma, outputFilename, fileOutputPNG);
                if(deletePFM){
                    Tracer.RemoveFile(outputFilename);
                }
            }

        } catch (Exception | InvalidMatrixException | InvalidPfmFileFormatException ex) {
            System.err.println(ex.getMessage());
        }
    }


    @Command(name = "convert", description = "Convert pfm file to png.")
    public void convert(
            @Parameters(index = "0", paramLabel = "-i", description = "String: Path of the input file") String inputFilename,
            //@Option(names = {"-i", "--inputFilename"}, description = "String: Path of the input file") String inputFilename,
            @Option(names = {"-o", "--outputFilename"}, description = "String: Path of the output ldr file") String outputFilename,
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

    @Override
    public void run() {
        CommandLine.usage(this, System.out);
    }
}
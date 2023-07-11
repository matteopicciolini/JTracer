package org.mirrors;

/**
 * This class contains the parameters given by command line like factor, monitor's gamma and input/output filename
 */
public class Parameters {
    public float factor;
    public float gamma;

    public Float luminosity;
    public String inputFileName;
    public String outputFileName;
    public String inputFileNameTXT;

    public int width;
    public int height;
    public float angleDeg;
    public String algorithm;
    public boolean orthogonal;
    public boolean antialiasing;
    public boolean parallelAntialiasing;
    public int nThreads;
    public boolean convertInPNG;
    public boolean deletePFM;
    public int progBarFlushFrequence;
    public int numOfRays;
    public int maxDepth;
    public int samplesPerSide;

    public int russianRouletteLimit;
    public Parameters(float factor, float gamma, String inputFileName, String outputFileName){
        this.factor = factor;
        this.gamma = gamma;
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }

    public Parameters(String inputFileNameTXT, int width, int height, float angleDeg,
                      String outputFileName, String algorithm, boolean antialiasing,
                      boolean parallelAntialiasing, int nThreads, boolean convertInPNG,
                      boolean deletePFM, int samplesPerSide, int progBarFlushFrequence, float gamma,
                      float factor, Float luminosity, int numOfRays, int maxDepth,
                      int russianRouletteLimit){
        this.inputFileNameTXT = inputFileNameTXT;
        this.width = width;
        this.height = height;
        this.angleDeg = angleDeg;
        this.algorithm = algorithm;
        this.outputFileName = outputFileName;
        this.antialiasing = antialiasing;
        this.parallelAntialiasing = parallelAntialiasing;
        this.nThreads = nThreads;
        this.convertInPNG = convertInPNG;
        this.deletePFM = deletePFM;
        this.samplesPerSide = samplesPerSide;
        this.progBarFlushFrequence = progBarFlushFrequence;
        this.gamma = gamma;
        this.factor = factor;
        this. luminosity = luminosity;
        this.numOfRays = numOfRays;
        this.maxDepth = maxDepth;
        this.russianRouletteLimit = russianRouletteLimit;
    }

    public Parameters(int width, int height, float angleDeg, String outputFileName,
                      boolean orthogonal, String algorithm, boolean antialiasing,
                      boolean parallelAntialiasing, int nThreads, boolean convertInPNG,
                      boolean deletePFM, int progBarFlushFrequence, float gamma,
                      float factor, Float luminosity, int numOfRays, int maxDepth,
                      int russianRouletteLimit){
        this.orthogonal = orthogonal;
        this.width = width;
        this.height = height;
        this.angleDeg = angleDeg;
        this.algorithm = algorithm;
        this.outputFileName = outputFileName;
        this.antialiasing = antialiasing;
        this.parallelAntialiasing = parallelAntialiasing;
        this.nThreads = nThreads;
        this.convertInPNG = convertInPNG;
        this.deletePFM = deletePFM;
        this.progBarFlushFrequence = progBarFlushFrequence;
        this.gamma = gamma;
        this.factor = factor;
        this. luminosity = luminosity;
        this.numOfRays = numOfRays;
        this.maxDepth = maxDepth;
        this.russianRouletteLimit = russianRouletteLimit;
    }


}

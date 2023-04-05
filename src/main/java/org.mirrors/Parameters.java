package org.mirrors;

/**
 * This class contains the parameters given by command line like factor, monitor's gamma and input/ output filename
 */
public class Parameters {
    public float factor;
    public float gamma;
    public String inputFileName;
    public String outputFileName;
    public Parameters(float factor, float gamma, String inputFileName, String outputFileName){
        this.factor = factor;
        this.gamma = gamma;
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
    }
}

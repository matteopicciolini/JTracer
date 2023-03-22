/**
 * This class contains the parameters given by command line like a factor, monitor's gamma and the input/ output filename
 */
public class Parameters {
    public float factor;
    public float gamma;
    public String inputfilename ;
    public String outputfilename ;
    public Parameters(float factor, float gamma, String inputfilename, String outputfilename){
        this.factor=factor;
        this.gamma=gamma;
        this.inputfilename=inputfilename;
        this.outputfilename=outputfilename;
    }
}

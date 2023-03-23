/**
 * This class contains the parameters given by command line like factor, monitor's gamma and input/ output filename
 */
public class Parameters {
    public float factor;
    public float gamma;
    public String input_file_name ;
    public String output_file_name ;
    public Parameters(float factor, float gamma, String input_file_name, String output_file_name){
        this.factor = factor;
        this.gamma = gamma;
        this.input_file_name = input_file_name;
        this.output_file_name = output_file_name;
    }
}

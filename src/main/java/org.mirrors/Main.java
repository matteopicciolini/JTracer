package org.mirrors;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Main {
    public static void main(String[] args){
        System.out.println("ciao!");
        /*Parameters param = new Parameters(Float.parseFloat(args[0]), Float.parseFloat(args[1]), args[2], args[3]);
        OutputStream out = new FileOutputStream(param.output_file_name);
        InputStream str = new FileInputStream(param.input_file_name);

        HDR_Image img = PfmCreator.read_pfm_image(str);

        img.normalize_image(param.factor);
        img.clamp_image();
        img.write_ldr_image(out, "PNG", param.gamma);*/
    }
}



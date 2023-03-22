import Exceptions.InvalidPfmFileFormat;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException, InvalidPfmFileFormat {

        parameters param = new parameters(Float.parseFloat(args[0]), Float.parseFloat(args[1]), args[2], args[3]);
        OutputStream out = new FileOutputStream(param.outputfilename);
        PfmCreator pfm = new PfmCreator();
        InputStream str = new FileInputStream("memorial.pfm");
        HDR_Image img = pfm.read_pfm_image(str);

        img.normalize_image(param.factor);
        img.clamp_image();
        img.write_ldr_image(out, "PNG", param.gamma);
    }
}



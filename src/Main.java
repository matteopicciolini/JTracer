import Exceptions.InvalidPfmFileFormat;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InvalidPfmFileFormat {
        System.out.println("Hello World!");
        PfmCreator.read_pfm_image(new ByteArrayInputStream("PF\n3 2\n-1.0\nstop".getBytes()));
    }
}


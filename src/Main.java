import Exceptions.InvalidPfmFileFormatException;
import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.Assertions;
import java.io.*;
import java.util.*;
import java.nio.ByteOrder;

import static org.junit.Assert.assertTrue;


public class Main {
    public static void main(String[] args) throws IOException, InvalidPfmFileFormatException {
        HDR_Image img = new HDR_Image();
        InputStream targetStream = new ByteArrayInputStream("PF\nworld".getBytes());
        //img.read_pfm_image(targetStream);


        /*HDR_Image img=new HDR_Image(3, 2);
        int i=0;
        FileReader is = new FileReader("reference_be.pfm");
        BufferedReader bf = new BufferedReader(is);
        ByteArrayOutputStream bf2 = new ByteArrayOutputStream();
        Vector v1=new Vector();*/
        String input=("3 2");
        String[] c = input.split(" ");
        int[] a = new int[2];
        a[0]= Integer.parseInt(String.valueOf(c[0]));
        a[1]= Integer.parseInt(String.valueOf(c[1]));


    }
}


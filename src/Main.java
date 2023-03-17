import com.sun.source.tree.AssertTree;
import org.junit.jupiter.api.Assertions;
import java.io.*;
import java.util.*;
import java.nio.ByteOrder;

import static org.junit.Assert.assertTrue;


public class Main {
    public static void main(String[] args) throws IOException {
        HDR_Image img=new HDR_Image(3, 2);
        int i=0;
        FileReader is = new FileReader("reference_be.pfm");
        BufferedReader bf = new BufferedReader(is);
        ByteArrayOutputStream bf2 = new ByteArrayOutputStream();
        Vector v1=new Vector();

        InputStream targetStream = new ByteArrayInputStream("Hello\nWorld".getBytes());
        InputStream inputStream=new FileInputStream("reference_be.pfm");
        while(bf.ready()){
            System.out.println(bf.readLine());
            bf2.write(bf.readLine().getBytes());
            i++;
        }
        System.out.println(bf2);


        /*
        ByteArrayOutputStream bytesOutput = new ByteArrayOutputStream();
        bytesOutput.write("Hello\nWorld".getBytes());
        byte[] bytes = bytesOutput.toByteArray();

        PipedInputStream pipedInputStream = new PipedInputStream();
        PipedOutputStream pipedOutputStream = new PipedOutputStream(pipedInputStream);
        pipedOutputStream.write(((ByteArrayOutputStream) bytesOutput).toByteArray());
        */

    }
}


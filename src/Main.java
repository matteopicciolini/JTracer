
import Exceptions.InvalidPfmFileFormat;
import Exceptions.InvalidPfmFileFormatException;

import java.nio.ByteOrder;
import java.io.*;



public class Main {
    public static void main(String[] args) throws IOException, InvalidPfmFileFormat, InvalidPfmFileFormatException {
    HDR_Image img=new HDR_Image();
    InputStream str = new FileInputStream("C:/Users/Francesco/IdeaProjects/ray_tracing/reference_le.pfm");
    String magic= img.read_line(str);
       // if (magic != "PF")
         //   throw new InvalidPfmFileFormat("invalid input format");

    String img_size=img.read_line(str);
    int[] size=new int[2];
    size[0]=img.parse_img_size(img_size)[0];
    size[1]=img.parse_img_size(img_size)[1];
    ByteOrder endianness_line;
    endianness_line=img.parse_endianness(img.read_line(str));
    HDR_Image img2= new HDR_Image(size[0], size[1]);



    System.out.println(img.read_float("a 55 78")[3]);
    }
}


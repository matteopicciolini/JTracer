import Exceptions.InvalidPfmFileFormat;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static java.nio.ByteOrder.BIG_ENDIAN;
import static org.junit.Assert.assertTrue;
public class HDR_Image {
    public int height;
    public int width;
    public Color[] pixels;

    public HDR_Image(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new Color[this.width * this.height];
    }

    public boolean valid_coordinates(int x, int y){
        return ((x >= 0) && (x < this.width) &&
                (y >= 0) && (y < this.height));
    }

    public int pixel_offset(int x, int y){
        return y * this.width + x;
    }

    public Color get_pixel(int x, int y) {
        assertTrue(this.valid_coordinates(x, y));
        return this.pixels[this.pixel_offset(x, y)];
    }

    public void set_pixel(int x, int y, Color color) {
        assertTrue(this.valid_coordinates(x, y));
        this.pixels[this.pixel_offset(x, y)] = color;
    }

    public void write_pfm(OutputStream stream, ByteOrder order) throws IOException {
        String endianness_str = (order == LITTLE_ENDIAN) ?  "-1.0" :  "1.0";
        String header = "PF\n" + this.width + " " + this.height + "\n" + endianness_str + "\n";
        byte [] bytes = header.getBytes(StandardCharsets.US_ASCII);
        stream.write(bytes, 0, bytes.length);

        Color color;
        for (int i = this.height - 1; i >= 0 ; --i){
            for(int j = 0; j < this.width; ++j){
                color = this.get_pixel(j, i);
                writeFloatToStream(stream, color.r, order);
                writeFloatToStream(stream, color.g, order);
                writeFloatToStream(stream, color.b, order);
            }
        }
    }

    private void writeFloatToStream(OutputStream stream, float value, ByteOrder order) throws IOException {
        //each float have 4 bytes. I allocate 4 bytes and if I use Litte_Endian, I reverse these bytes
        byte[] bytes = ByteBuffer.allocate(4).putFloat(value).array();
        if (order == LITTLE_ENDIAN){
            Functions_Constants.byte_reverse(bytes, 4);
        }
        stream.write(bytes);

    }

    protected static String read_line(InputStream targetStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int nextByte;
        while ((nextByte = targetStream.read()) != -1) {
            if (nextByte == '\n') {
                break;
            }
            outputStream.write(nextByte);
        }
        return outputStream.toString();
    }

    public static int[] parse_img_size(String line) throws InvalidPfmFileFormat {
        String[] elements = line.split(" ");
        if (elements.length != 2)
            throw new InvalidPfmFileFormat("Invalid image size specification");
        try {
            int width = Integer.parseInt(elements[0]);
            int height = Integer.parseInt(elements[1]);
            if (width < 0 || height < 0) {
                throw new NumberFormatException();
            }
            return new int[]{width, height};
        }
        catch (NumberFormatException e){
            throw new InvalidPfmFileFormat("Invalid width/height");
        }
    }

    protected static ByteOrder parse_endianness(String line) throws InvalidPfmFileFormat {
        float value;
        try {
            value = Float.parseFloat(line);
        } catch (NumberFormatException e) {
            throw new InvalidPfmFileFormat("Missing endianness specification.");
        }
        if (value > 0){
            return BIG_ENDIAN;
        } else if (value < 0) {
            return LITTLE_ENDIAN;
        } else{
            throw new InvalidPfmFileFormat("Invalid endianness specification, it cannot be zero.");
        }
    }

    private static float readFloat(InputStream stream, ByteOrder endianness) throws InvalidPfmFileFormat {
        byte[] floatBytes = new byte[4];
        try {
            // Read 4 bytes from the input stream
            DataInputStream DataStream = new DataInputStream(stream);
            DataStream.readFully(floatBytes);

            if (endianness == BIG_ENDIAN) {
                return ByteBuffer.wrap(floatBytes).order(BIG_ENDIAN).getFloat();
            }else {
                return ByteBuffer.wrap(floatBytes).order(LITTLE_ENDIAN).getFloat();
            }
        } catch (IOException e) {
            throw new InvalidPfmFileFormat("Impossible to read binary data from the file");
        }
    }

    protected static HDR_Image read_pfm_image(InputStream stream) throws IOException, InvalidPfmFileFormat {

        String magic = read_line(stream);
        if (!magic.equals("PF")) throw new InvalidPfmFileFormat("Invalid magic in PFM file");

        String img_size = read_line(stream);
        int [] dim = parse_img_size(img_size);

        String endianness_line = read_line(stream);
        ByteOrder endianness = parse_endianness(endianness_line);

        HDR_Image result = new HDR_Image(dim[0], dim[1]);
        float r, g, b;
        for (int i = result.height - 1; i >= 0 ; --i) {
            for (int j = 0; j < result.width; ++j) {
                r = readFloat(stream, endianness);
                g = readFloat(stream, endianness);
                b = readFloat(stream, endianness);
                result.set_pixel(j, i, new Color(r, g, b));
            }
        }
        return result;
    }
}
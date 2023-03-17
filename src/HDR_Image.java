import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.junit.Assert.assertTrue;
public class HDR_Image {
    public int height;
    public int width;
    public Color[] pixels;
    public HDR_Image() {
        this.width = 0;
        this.height = 0;
        this.pixels = new Color[0];
    }

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

    protected static ByteArrayOutputStream read_line(InputStream targetStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int nextByte;
        while ((nextByte = targetStream.read()) != -1) {
            if (nextByte == '\n') {
                break;
            }
            outputStream.write(nextByte);
        }
        return outputStream;
    }
    protected static ByteArrayOutputStream read_float(InputStream inputStream) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        float rif = -1.0f;
        while ((rif = inputStream.read()) != -1) {
            out.write((byte)rif);

        }
        return out;
    }
    protected int[] parse_img_size(String line) throws IOException {
        String input = (line);
        int[] a = new int[2];
        String[] c = input.split(" ");
        if (line.length() != 2)
            System.out.println("invalid image size specification");
        try {
            a[0] = Integer.parseInt(String.valueOf(c[0]));
            a[1] = Integer.parseInt(String.valueOf(c[1]));

            this.width = (a[0]);
            this.height = (a[1]);
        } catch (Exception e){
            
        }
        return a;
    }


}
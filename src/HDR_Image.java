import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.junit.Assert.assertTrue;

/**
 * Class used to build an HDR image with the following public variables: number of rows & columns and
 * the intensity of the three colors RGB for every pixel
 */
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

    /**
     * Metodo volto alla scrittura di un file pfm.
     * Utilizza uno stream per scrivere il contenuto ndella stringa che si vuole scrivere in formato binario e
     */
    public void write_pfm(OutputStream stream, ByteOrder order) throws IOException {

        String endianness_str = (order == LITTLE_ENDIAN) ?  "-1.0" :  "1.0";
        byte[] bytes = String.format("PF\n%d %d\n%s\n", this.width, this.height, endianness_str).getBytes(StandardCharsets.US_ASCII);
        stream.write(bytes, 0 , bytes.length);
        Color color;

        for (int i = this.height - 1; i >= 0 ; --i){
            for(int j = 0; j < this.width; ++j){
                color = this.get_pixel(j, i);
                WriteFloatToStream(stream, color.r, order);
                WriteFloatToStream(stream, color.g, order);
                WriteFloatToStream(stream, color.b, order);
            }
        }
    }

    private void WriteFloatToStream(OutputStream stream, float value, ByteOrder order) throws IOException {
        byte[] floatBytes = ByteBuffer.allocate(4).order(order).putFloat(value).array();
        stream.write(floatBytes);

    }

}
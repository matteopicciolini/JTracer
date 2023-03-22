import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import static java.lang.Math.log10;
import static java.lang.Math.pow;
import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static org.junit.Assert.assertTrue;

/**
 * Class used to build an HDR_Image object with the following public variables: number of rows & columns and
 * the intensity of the three colors RGB for every pixel.
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

    public float average_luminosity(float delta){
        float cum_sum = 0.0f;
        for (Color pix : this.pixels){
            cum_sum += log10(delta + pix.luminosity());
        }
        return (float) pow(10, cum_sum / pixels.length);
    }

    public void normalize_image(float factor, Float luminosity){


        for (int i =0; i<this.width*this.height; i++){
            this.pixels[i].r=this.pixels[i].r*(factor/luminosity);
            this.pixels[i].g=this.pixels[i].g*(factor/luminosity);
            this.pixels[i].b=this.pixels[i].b*(factor/luminosity);
        }
    }
    public void normalize_image(float factor) {
        normalize_image(factor, average_luminosity(1e-5f));
    }

    public float average_luminosity(){
        return average_luminosity(1e-10f);
    }

    private float clamp(float x){
        return x / (1 + x);
    }

    public void clamp_image(){
        for (Color pixel : this.pixels) {
            pixel.r = this.clamp(pixel.r);
            pixel.g = this.clamp(pixel.g);
            pixel.b = this.clamp(pixel.b);
        }
    }

    /**
     * This function is used to write a ldr (jpg or png) image.
     * It writes in a buffer the entire pixels array and print them in a jpg/png image using the library javax.imageIO.
     * ist sees the RGB triad as a 32 but int filled, where the representation looks as 00000000 rrrrrrrr gggggggg bbbbbbbb
     * @param stream
     * @param format
     * @param gamma
     * @throws IOException
     */
    public void write_ldr_image(OutputStream stream, String format, float gamma) throws IOException {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < this.height; ++i) {
            for (int j = 0; j < this.width; ++j) {
                Color cur_color = this.get_pixel(j, i);
                int r = (int) (255 * Math.pow(cur_color.r , 1.0 / gamma));
                int g = (int) (255 * Math.pow(cur_color.g , 1.0 / gamma));
                int b = (int) (255 * Math.pow(cur_color.b , 1.0 / gamma));
                img.setRGB(j, i, (r << 16) + (g << 8) + b);
            }
        }
        ImageIO.write(img, format, stream);
    }
}


package org.mirrors;

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

/**
 * Class used to build an HDRImage object with the following properties:
 *  - [width] - Number of columns in the matrix of colors
 *  - [height] - Number of rows in the matrix of colors
 *  - [pixels] - 1D array representing the matrix of colors (RGB)
 *  @See Color
 */
public class HDRImage {
    public int height;
    public int width;
    public Color[] pixels;

    public HDRImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new Color[this.width * this.height];
    }

    public boolean validCoordinates(int x, int y){
        return ((x >= 0) && (x < this.width) &&
                (y >= 0) && (y < this.height));
    }

    public int pixelOffset(int x, int y){
        return y * this.width + x;
    }

    public Color getPixel(int x, int y) {
        if(!validCoordinates(x, y)){
            throw new RuntimeException("Invalid pixel coordinates");
        }
        return this.pixels[this.pixelOffset(x, y)];
    }

    public void setPixel(int x, int y, Color color) {
        if(!validCoordinates(x, y)){
            throw new RuntimeException("Invalid pixel coordinates");
        }
        this.pixels[this.pixelOffset(x, y)] = color;
    }

    public void writePfm(OutputStream stream, ByteOrder order) throws IOException {

        String endianness_str = (order == LITTLE_ENDIAN) ?  "-1.0" :  "1.0";
        byte[] bytes = String.format("PF\n%d %d\n%s\n", this.width, this.height, endianness_str).getBytes(StandardCharsets.US_ASCII);
        stream.write(bytes, 0 , bytes.length);
        Color color;

        for (int i = this.height - 1; i >= 0 ; --i){
            for(int j = 0; j < this.width; ++j){
                color = this.getPixel(j, i);
                writeFloatToStream(stream, color.r, order);
                writeFloatToStream(stream, color.g, order);
                writeFloatToStream(stream, color.b, order);
            }
        }
    }

    private void writeFloatToStream(OutputStream stream, float value, ByteOrder order) throws IOException {
        byte[] floatBytes = ByteBuffer.allocate(4).order(order).putFloat(value).array();
        stream.write(floatBytes);
    }

    public float averageLuminosity(float delta){
        float cum_sum = 0.0f;
        for (Color pix : this.pixels){
            cum_sum += log10(delta + pix.luminosity());
        }
        return (float) pow(10, cum_sum / pixels.length);
    }

    public void normalizeImage(float factor, Float luminosity){
        for (int i = 0; i < this.width * this.height; ++i){
            this.pixels[i].r = this.pixels[i].r * (factor/luminosity);
            this.pixels[i].g = this.pixels[i].g * (factor/luminosity);
            this.pixels[i].b = this.pixels[i].b * (factor/luminosity);
        }
    }
    public void normalizeImage(float factor) {
        normalizeImage(factor, averageLuminosity(1e-5f));
    }

    public float averageLuminosity(){
        return averageLuminosity(1e-10f);
    }

    private float clamp(float x){
        return x / (1 + x);
    }

    public void clampImage(){
        for (Color pixel : this.pixels) {
            pixel.r = this.clamp(pixel.r);
            pixel.g = this.clamp(pixel.g);
            pixel.b = this.clamp(pixel.b);
        }
    }

    /**
     * This function is used to write a ldr (jpg or png) image.
     * It writes in the buffer the entire pixels array and create a file jpg/png image using javax.imageIO.
     * @param stream - output stream
     * @param format - format of the image e.g. 'PNG', 'JPG'
     * @param gamma - gamma function
     * @throws IOException - throws IOException exception
     */
    public void writeLdrImage(OutputStream stream, String format, float gamma) throws IOException {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < this.height; ++i) {
            for (int j = 0; j < this.width; ++j) {
                Color cur_color = this.getPixel(j, i);
                int r = (int) (255f * Math.pow(cur_color.r , 1.0f / gamma));
                int g = (int) (255f * Math.pow(cur_color.g , 1.0f / gamma));
                int b = (int) (255f * Math.pow(cur_color.b , 1.0f / gamma));

                img.setRGB(j, i, (r << 16) + (g << 8) + b);
                //third parameter is an 32 bit int RGB with this representation: 00000000 rrrrrrrr gggggggg bbbbbbbb
            }
        }
        ImageIO.write(img, format, stream);
    }
}


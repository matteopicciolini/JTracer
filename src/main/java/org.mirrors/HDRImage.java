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
 * The HDRImage class represents a high dynamic range (HDR) image. This class provides methods for manipulating and
 * processing the image, such as getting and setting individual pixels, writing the image to a Portable Float Map (PFM)
 * file, normalizing the image, averaging the luminosity of the image, clamping the image, and writing a low dynamic
 * range (LDR) image in PNG or JPG format. An HDR image is represented as an array of Color objects, where each Color
 * object represents the red, green, and blue channels of a pixel.
 */
public class HDRImage {
    public int height;
    public int width;
    public Color[] pixels;

    /**
     * Constructs a new HDRImage object with the specified width and height.
     *
     * @param width  The width of the new image.
     * @param height The height of the new image.
     */
    public HDRImage(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new Color[this.width * this.height];
    }

    /**
     * Checks if the specified coordinates are valid for this image.
     *
     * @param x The x-coordinate of the pixel.
     * @param y The y-coordinate of the pixel.
     * @return true if the coordinates are valid, false otherwise.
     */
    public boolean validCoordinates(int x, int y){
        return ((x >= 0) && (x < this.width) &&
                (y >= 0) && (y < this.height));
    }

    /**
     * Calculates the offset of the specified pixel in the 1D array of pixel data.
     *
     * @param x The x-coordinate of the pixel.
     * @param y The y-coordinate of the pixel.
     * @return The offset of the specified pixel in the pixel array.
     */
    public int pixelOffset(int x, int y){
        return y * this.width + x;
    }

    /**
     * Returns the Color object representing the pixel at the specified coordinates.
     *
     * @param x The x-coordinate of the pixel.
     * @param y The y-coordinate of the pixel.
     * @return The Color object representing the pixel at the specified coordinates.
     * @throws RuntimeException if the coordinates are invalid.
     */
    public Color getPixel(int x, int y) {
        if(!validCoordinates(x, y)){
            throw new RuntimeException("Invalid pixel coordinates");
        }
        return this.pixels[this.pixelOffset(x, y)];
    }

    /**
     * Sets the pixel at the specified coordinates to the specified Color object.
     *
     * @param x     The x-coordinate of the pixel.
     * @param y     The y-coordinate of the pixel.
     * @param color The new color of the pixel.
     * @throws RuntimeException if the coordinates are invalid.
     */
    public void setPixel(int x, int y, Color color) {
        if(!validCoordinates(x, y)){
            throw new RuntimeException("Invalid pixel coordinates");
        }
        this.pixels[this.pixelOffset(x, y)] = color;
    }

    /**
     * Writes the image to a Portable Float Map (PFM) file with the specified byte order.
     *
     * @param stream The output stream to write the PFM data to.
     * @param order  The byte order to use (either ByteOrder.LITTLE_ENDIAN or ByteOrder.BIG_ENDIAN).
     * @throws IOException if there is an error writing the PFM data.
     */
    public void writePfm(OutputStream stream, ByteOrder order) throws IOException {

        String endianness_str = (order == LITTLE_ENDIAN) ?  "-1.0" : "1.0";
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

    /**
     * Writes a float value to the specified output stream using the specified byte order.
     *
     * @param stream The output stream to write the float value to.
     * @param value  The float value to write.
     * @param order  The byte order to use (either ByteOrder.LITTLE_ENDIAN or ByteOrder.BIG_ENDIAN).
     * @throws IOException if there is an error writing the float value.
     */
    private void writeFloatToStream(OutputStream stream, float value, ByteOrder order) throws IOException {
        byte[] floatBytes = ByteBuffer.allocate(4).order(order).putFloat(value).array();
        stream.write(floatBytes);
    }

    /**
     * Calculates the average luminosity of the image using the specified delta value.
     *
     * @param delta The delta value to use in the calculation.
     * @return The average luminosity of the image.
     */
    public float averageLuminosity(float delta){
        float cum_sum = 0.0f;
        for (Color pix : this.pixels){
            cum_sum += log10(delta + pix.luminosity());
        }
        return (float) pow(10, cum_sum / pixels.length);
    }

    /**
     * Normalizes the image by scaling each pixel value by the specified factor divided by the specified luminosity value.
     *
     * @param factor     The factor to scale each pixel value by.
     * @param luminosity The luminosity value to use in the normalization calculation.
     */
    public void normalizeImage(float factor, Float luminosity){
        for (int i = 0; i < this.width * this.height; ++i){
            this.pixels[i].r = this.pixels[i].r * (factor/luminosity);
            this.pixels[i].g = this.pixels[i].g * (factor/luminosity);
            this.pixels[i].b = this.pixels[i].b * (factor/luminosity);
        }
    }

    /**
     * Normalizes the image by scaling each pixel value by the specified factor divided by the average luminosity of the image.
     *
     * @param factor The factor to scale each pixel value by.
     */
    public void normalizeImage(float factor) {
        normalizeImage(factor, averageLuminosity(1e-5f));
    }

    /**
     * Calculates the average luminosity of the image using a delta value of 1e-10.
     *
     * @return The average luminosity of the image.
     */
    public float averageLuminosity(){
        return averageLuminosity(1e-10f);
    }

    private float clamp(float x){
        return x / (1 + x);
    }

    /**
     * Applies the clamp operator to each pixel value in the image.
     */
    public void clampImage(){
        for (Color pixel : this.pixels) {
            pixel.r = this.clamp(pixel.r);
            pixel.g = this.clamp(pixel.g);
            pixel.b = this.clamp(pixel.b);
        }
    }

    /**
     * Writes the image to a low dynamic range (LDR) image file in PNG or JPG format.
     *
     * @param stream The output stream to write the LDR data to.
     * @param format The format of the LDR image file (either "PNG" or "JPG").
     * @param gamma  The gamma function to use in the conversion from HDR to LDR.
     * @throws IOException if there is an error writing the LDR data.
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


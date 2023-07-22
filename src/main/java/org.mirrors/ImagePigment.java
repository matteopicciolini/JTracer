package org.mirrors;

/**
 * Represents a pigment that uses an HDRImage for color.
 */
public class ImagePigment extends Pigment {
    HDRImage image;

    /**
     * Constructs an ImagePigment with the specified HDRImage.
     *
     * @param image the HDRImage used for color
     */
    public ImagePigment(HDRImage image) {
        this.image = image;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Color getColor(float u, float v) {
        int col = (int) (u * this.image.width);
        int row = (int) (v * this.image.height);

        if (col >= this.image.width) {
            col = this.image.width - 1;
        }
        if (row >= this.image.height) {
            row = this.image.height - 1;
        }
        return this.image.getPixel(col, row);
    }
}
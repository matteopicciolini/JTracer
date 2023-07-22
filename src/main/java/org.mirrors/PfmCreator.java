package org.mirrors;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

/**
 * This class provides utility functions for reading and parsing Portable FloatMap (PFM) files
 * and creating an HDR image object from the data.
 */
public class PfmCreator {

    /**
     * Reads a line of text from an input stream and returns it as a String.
     *
     * @param targetStream The input stream to read from.
     * @return The line of text as a String.
     * @throws IOException if an I/O error occurs while reading from the stream.
     */
    public static String readLine(InputStream targetStream) throws IOException {
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

    /**
     * Parses the image size specification string and returns an integer array containing the width
     * and height of the image.
     *
     * @param line The image size specification string.
     * @return An integer array containing the width and height of the image.
     * @throws InvalidPfmFileFormatException if the image size specification is invalid.
     */
    public static int[] parseImgSize(String line) throws InvalidPfmFileFormatException {
        String[] elements = line.split(" ");
        if (elements.length != 2)
            throw new InvalidPfmFileFormatException("Invalid image size specification");
        try {
            int width = Integer.parseInt(elements[0]);
            int height = Integer.parseInt(elements[1]);
            if (width < 0 || height < 0) {
                throw new NumberFormatException();
            }
            return new int[]{width, height};
        } catch (NumberFormatException e) {
            throw new InvalidPfmFileFormatException("Invalid width/height");
        }
    }

    /**
     * Parses the endianness specification string and returns the corresponding ByteOrder enum value.
     *
     * @param line The endianness specification string.
     * @return The corresponding ByteOrder enum value.
     * @throws InvalidPfmFileFormatException if the endianness specification is invalid or missing.
     */
    public static ByteOrder parseEndianness(String line) throws InvalidPfmFileFormatException {
        float value;
        try {
            value = Float.parseFloat(line);
        } catch (NumberFormatException e) {
            throw new InvalidPfmFileFormatException("Missing endianness specification.");
        }
        if (value > 0) {
            return BIG_ENDIAN;
        } else if (value < 0) {
            return LITTLE_ENDIAN;
        } else {
            throw new InvalidPfmFileFormatException("Invalid endianness specification, it cannot be zero.");
        }
    }

    /**
     * Reads a single floating point value from an input stream using the specified endianness.
     *
     * @param stream     The input stream to read from.
     * @param endianness The endianness to use.
     * @return The floating point value.
     * @throws InvalidPfmFileFormatException if the binary data cannot be read from the stream.
     */
    private static float readFloat(InputStream stream, ByteOrder endianness) throws InvalidPfmFileFormatException {
        byte[] floatBytes = new byte[4];
        try {
            // Read 4 bytes from the input stream
            DataInputStream DataStream = new DataInputStream(stream);
            DataStream.readFully(floatBytes);

            if (endianness == BIG_ENDIAN) {
                return ByteBuffer.wrap(floatBytes).order(BIG_ENDIAN).getFloat();
            } else {
                return ByteBuffer.wrap(floatBytes).order(LITTLE_ENDIAN).getFloat();
            }
        } catch (IOException e) {
            throw new InvalidPfmFileFormatException("Impossible to read binary data from the file");
        }
    }

    /**
     * The main function of PfmCreator class.
     * Reads a PFM image file from an input stream and returns the corresponding HDRImage object.
     *
     * @param stream The input stream to read from.
     * @return An HDRImage object containing the image data.
     * @throws IOException                   if an I/O error occurs while reading from the stream.
     * @throws InvalidPfmFileFormatException if the PFM file format is invalid.
     */
    public static HDRImage readPfmImage(InputStream stream) throws IOException, InvalidPfmFileFormatException {
        String magic = readLine(stream);
        if (!magic.equals("PF")) throw new InvalidPfmFileFormatException("Invalid magic in PFM file");

        String img_size = readLine(stream);
        int[] dim = parseImgSize(img_size);

        String endianness_line = readLine(stream);
        ByteOrder endianness = parseEndianness(endianness_line);

        HDRImage result = new HDRImage(dim[0], dim[1]);
        float r, g, b;
        for (int i = result.height - 1; i >= 0; --i) {
            for (int j = 0; j < result.width; ++j) {
                r = readFloat(stream, endianness);
                g = readFloat(stream, endianness);
                b = readFloat(stream, endianness);
                result.setPixel(j, i, new Color(r, g, b));
            }
        }
        return result;
    }
}

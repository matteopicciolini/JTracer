import Exceptions.InvalidPfmFileFormat;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

/**
 * Class used to build the read_pfm_image function containing 4 sub-functions used in the main function.
 */
public class PfmCreator {
    public static String read_line(InputStream targetStream) throws IOException {
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

    public static ByteOrder parse_endianness(String line) throws InvalidPfmFileFormat {
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

    /**
     * The main function of PfmCreator Class. It reads separately every line from the pfm file, and it assigns
     * every pixel triad RGB read in the file to the HDR_image variable defined in the main.
     * @param stream
     * @return HDR_image
     * @throws IOException
     * @throws InvalidPfmFileFormat
     */
    public static HDR_Image read_pfm_image(InputStream stream) throws IOException, InvalidPfmFileFormat {

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
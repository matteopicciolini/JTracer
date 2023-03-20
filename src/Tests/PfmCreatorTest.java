import Exceptions.InvalidPfmFileFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

class PfmCreatorTest {
    @Test
    void parse_endianness() throws InvalidPfmFileFormat {
        Assertions.assertSame(PfmCreator.parse_endianness(("1.0")), BIG_ENDIAN);
        Assertions.assertSame(PfmCreator.parse_endianness(("-1.0")), LITTLE_ENDIAN);

        try {
            PfmCreator.parse_endianness("0.0");
            Assertions.fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormat e) {
            // Exception was thrown as expected
        }

        try {
            PfmCreator.parse_endianness("abc");
            Assertions.fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormat e) {
            // Exception was thrown as expected
        }
    }

    @Test
    void parse_img_size() throws InvalidPfmFileFormat {

        int[] expectedSize = {3, 2};
        int[] actualSize = PfmCreator.parse_img_size("3 2");
        Assertions.assertArrayEquals(expectedSize, actualSize);

        try {
            PfmCreator.parse_img_size("-1 3");
            Assertions.fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormat e) {
            // Exception was thrown as expected
        }

        try {
            PfmCreator.parse_img_size("3 2 1");
            Assertions.fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormat e) {
            // Exception was thrown as expected
        }
    }

    @Test
    void read_pfm_image() throws IOException, InvalidPfmFileFormat {
        byte[][] bytes_arrays = new byte[2][];
        bytes_arrays[0] = Global.LE_reference_bytes;
        bytes_arrays[1] = Global.BE_reference_bytes;
        for (byte [] bytes_array : bytes_arrays){
            HDR_Image img = PfmCreator.read_pfm_image(new ByteArrayInputStream(bytes_array));
            Assertions.assertEquals(3, img.width);
            Assertions.assertEquals(2, img.height);

            Assertions.assertTrue(img.get_pixel(0, 0).is_close(new Color(1.0e1f, 2.0e1f, 3.0e1f)));
            Assertions.assertTrue(img.get_pixel(1, 0).is_close(new Color(4.0e1f, 5.0e1f, 6.0e1f)));
            Assertions.assertTrue(img.get_pixel(2, 0).is_close(new Color(7.0e1f, 8.0e1f, 9.0e1f)));
            Assertions.assertTrue(img.get_pixel(0, 1).is_close(new Color(1.0e2f, 2.0e2f, 3.0e2f)));
            Assertions.assertTrue(img.get_pixel(0, 0).is_close(new Color(1.0e1f, 2.0e1f, 3.0e1f)));
            Assertions.assertTrue(img.get_pixel(1, 1).is_close(new Color(4.0e2f, 5.0e2f, 6.0e2f)));
            Assertions.assertTrue(img.get_pixel(2, 1).is_close(new Color(7.0e2f, 8.0e2f, 9.0e2f)));
        }
    }

    @Test
    void read_pfm_image_wrong() throws IOException {
        try {
            PfmCreator.read_pfm_image(new ByteArrayInputStream("PF\n3 2\n-1.0\nstop".getBytes()));
            Assertions.fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormat e) {
            // Exception was thrown as expected
        }
    }
}
import Exceptions.InvalidPfmFileFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

class HDR_ImageTest {

    @Test
    void valid_coordinates() {
        HDR_Image img = new HDR_Image(7, 4);
        Assertions.assertTrue(img.valid_coordinates(0, 0));
        Assertions.assertTrue(img.valid_coordinates(6, 3));
        Assertions.assertFalse(img.valid_coordinates(-1, 0));
        Assertions.assertFalse(img.valid_coordinates(0, -1));
        Assertions.assertFalse(img.valid_coordinates(7, 0));
        Assertions.assertFalse(img.valid_coordinates(0, 4));
    }

    @Test
    void pixel_offset() {
        HDR_Image img = new HDR_Image(7, 4);
        Assertions.assertEquals(17, img.pixel_offset(3, 2));
        Assertions.assertEquals(7 * 4 - 1, img.pixel_offset(6, 3));
    }

    @Test
    void get_set_pixel() {
        HDR_Image img = new HDR_Image(7, 4);
        Color color = new Color(1.0f, 2.0f, 3.0f);
        img.set_pixel(3, 2, color);
        Assertions.assertTrue(color.is_close(img.get_pixel(3, 2)));
    }

    @Test
    void write_pfm() throws IOException {
        HDR_Image img = new HDR_Image(3, 2);

        Color color1 = new Color(1.0e1f, 2.0e1f, 3.0e1f);
        Color color2 = new Color(4.0e1f, 5.0e1f, 6.0e1f);
        Color color3 = new Color(7.0e1f, 8.0e1f, 9.0e1f);
        Color color4 = new Color(1.0e2f, 2.0e2f, 3.0e2f);
        Color color5 = new Color(4.0e2f, 5.0e2f, 6.0e2f);
        Color color6 = new Color(7.0e2f, 8.0e2f, 9.0e2f);

        img.set_pixel(0, 0, color1);
        img.set_pixel(1, 0, color2);
        img.set_pixel(2, 0, color3);
        img.set_pixel(0, 1, color4);
        img.set_pixel(1, 1, color5);
        img.set_pixel(2, 1, color6);

        ByteArrayOutputStream stream_lit = new ByteArrayOutputStream();
        img.write_pfm(stream_lit, ByteOrder.LITTLE_ENDIAN);
        Assertions.assertTrue(Functions_Constants.match(stream_lit, Functions_Constants.LE_reference_bytes));

        ByteArrayOutputStream stream_big = new ByteArrayOutputStream();
        img.write_pfm(stream_big, ByteOrder.BIG_ENDIAN);
        Assertions.assertTrue(Functions_Constants.match(stream_big, Functions_Constants.BE_reference_bytes));
    }
    @Test
    void read_line() throws IOException {
        InputStream targetStream = new ByteArrayInputStream("Hello\nWorld".getBytes());
        Assertions.assertEquals(HDR_Image.read_line(targetStream), "Hello");
        Assertions.assertEquals(HDR_Image.read_line(targetStream), "World");
    }

    @Test
    void parse_endianness() throws InvalidPfmFileFormat {
        Assertions.assertTrue(HDR_Image.parse_endianness(("1.0")) == BIG_ENDIAN);
        Assertions.assertTrue(HDR_Image.parse_endianness(("-1.0")) == LITTLE_ENDIAN);

        try {
            HDR_Image.parse_endianness("0.0");
            Assertions.fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormat e) {
            // Exception was thrown as expected
        }

        try {
            HDR_Image.parse_endianness("abc");
            Assertions.fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormat e) {
            // Exception was thrown as expected
        }
    }

    @Test
    void parse_img_size() throws InvalidPfmFileFormat {

        int[] expectedSize = {3, 2};
        int[] actualSize = HDR_Image.parse_img_size("3 2");
        Assertions.assertArrayEquals(expectedSize, actualSize);

        try {
            HDR_Image.parse_img_size("-1 3");
            Assertions.fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormat e) {
            // Exception was thrown as expected
        }

        try {
            HDR_Image.parse_img_size("3 2 1");
            Assertions.fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormat e) {
            // Exception was thrown as expected
        }
    }

    @Test
    void read_pfm_image() throws IOException, InvalidPfmFileFormat {
        byte[][] bytes_arrays = new byte[2][];
        bytes_arrays[0] = Functions_Constants.LE_reference_bytes;
        bytes_arrays[1] = Functions_Constants.BE_reference_bytes;
        for (byte [] bytes_array : bytes_arrays){
            HDR_Image img = HDR_Image.read_pfm_image(new ByteArrayInputStream(bytes_array));
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
            HDR_Image.read_pfm_image(new ByteArrayInputStream("PF\n3 2\n-1.0\nstop".getBytes()));
            Assertions.fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormat e) {
            // Exception was thrown as expected
        }
    }
}

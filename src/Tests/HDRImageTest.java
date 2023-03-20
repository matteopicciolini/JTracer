import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

class HDRImageTest {

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

        img.set_pixel(0, 0, new Color(1.0e1f, 2.0e1f, 3.0e1f));
        img.set_pixel(1, 0, new Color(4.0e1f, 5.0e1f, 6.0e1f));
        img.set_pixel(2, 0, new Color(7.0e1f, 8.0e1f, 9.0e1f));
        img.set_pixel(0, 1, new Color(1.0e2f, 2.0e2f, 3.0e2f));
        img.set_pixel(1, 1, new Color(4.0e2f, 5.0e2f, 6.0e2f));
        img.set_pixel(2, 1, new Color(7.0e2f, 8.0e2f, 9.0e2f));

        ByteArrayOutputStream stream_lit = new ByteArrayOutputStream();
        img.write_pfm(stream_lit, ByteOrder.LITTLE_ENDIAN);
        Assertions.assertArrayEquals(stream_lit.toByteArray(), Global.LE_reference_bytes);

        ByteArrayOutputStream stream_big = new ByteArrayOutputStream();
        img.write_pfm(stream_big, ByteOrder.BIG_ENDIAN);
        Assertions.assertArrayEquals(stream_big.toByteArray(), Global.BE_reference_bytes);
    }
    @Test
    void read_line() throws IOException {
        InputStream targetStream = new ByteArrayInputStream("Hello\nWorld".getBytes());
        Assertions.assertEquals(PfmCreator.read_line(targetStream), "Hello");
        Assertions.assertEquals(PfmCreator.read_line(targetStream), "World");
    }
}

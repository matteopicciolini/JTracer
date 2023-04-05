package org.mirrors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

class HDRImageTest {

    @Test
    void valid_coordinates() {
        HDRImage img = new HDRImage(7, 4);
        assertTrue(img.validCoordinates(0, 0));
        assertTrue(img.validCoordinates(6, 3));
        assertFalse(img.validCoordinates(-1, 0));
        assertFalse(img.validCoordinates(0, -1));
        assertFalse(img.validCoordinates(7, 0));
        assertFalse(img.validCoordinates(0, 4));
    }

    @Test
    void pixel_offset() {
        HDRImage img = new HDRImage(7, 4);
        assertEquals(17, img.pixelOffset(3, 2));
        assertEquals(7 * 4 - 1, img.pixelOffset(6, 3));
    }

    @Test
    void get_set_pixel() {
        HDRImage img = new HDRImage(7, 4);
        Color color = new Color(1.0f, 2.0f, 3.0f);
        img.setPixel(3, 2, color);
        assertTrue(color.isClose(img.getPixel(3, 2)));
    }

    @Test
    void write_pfm() throws IOException {
        HDRImage img = new HDRImage(3, 2);

        img.setPixel(0, 0, new Color(1.0e1f, 2.0e1f, 3.0e1f));
        img.setPixel(1, 0, new Color(4.0e1f, 5.0e1f, 6.0e1f));
        img.setPixel(2, 0, new Color(7.0e1f, 8.0e1f, 9.0e1f));
        img.setPixel(0, 1, new Color(1.0e2f, 2.0e2f, 3.0e2f));
        img.setPixel(1, 1, new Color(4.0e2f, 5.0e2f, 6.0e2f));
        img.setPixel(2, 1, new Color(7.0e2f, 8.0e2f, 9.0e2f));

        ByteArrayOutputStream stream_lit = new ByteArrayOutputStream();
        img.writePfm(stream_lit, ByteOrder.LITTLE_ENDIAN);
        assertArrayEquals(stream_lit.toByteArray(), Global.LE_ReferenceBytes);

        ByteArrayOutputStream stream_big = new ByteArrayOutputStream();
        img.writePfm(stream_big, ByteOrder.BIG_ENDIAN);
        assertArrayEquals(stream_big.toByteArray(), Global.BE_ReferenceBytes);
    }
    @Test
    void read_line() throws IOException {
        InputStream targetStream = new ByteArrayInputStream("Hello\nWorld".getBytes());
        assertEquals(PfmCreator.readLine(targetStream), "Hello");
        assertEquals(PfmCreator.readLine(targetStream), "World");
    }

    @Test
    void normalize_image() {
        HDRImage img = new HDRImage(2, 1);
        Color color1 = new Color(5.0f, 10.0f, 15.0f);
        Color color2 = new Color(500.0f, 1000.0f, 1500.0f);
        img.setPixel(0, 0, color1);
        img.setPixel(1, 0, color2);
        img.normalizeImage(1000.0f, 100.0f);

        Color final1 = new Color(0.5e2f, 1.0e2f, 1.5e2f);
        Color final2 = new Color(0.5e4f, 1.0e4f, 1.5e4f);

        assertTrue(final1.isClose(img.getPixel(0, 0)));
        assertTrue(final2.isClose(img.getPixel(1, 0)));
    }

    @Test
    void average_luminosity() {
        HDRImage img = new HDRImage(2, 1);
        img.setPixel(0, 0, new Color(5.0f, 10.0f, 15.0f));
        img.setPixel(1, 0, new Color(500.0f, 1000.0f, 1500.0f));
        assertEquals(100.0f, img.averageLuminosity(0.0f), 1e-5);
    }

    @Test
    void clamp_image() {
        HDRImage img = new HDRImage(2, 1);
        img.setPixel(0, 0, new Color(5.0f, 10.0f, 15.0f));
        img.setPixel(1, 0, new Color(500.0f, 1000.0f, 1500.0f));
        img.clampImage();
        for(Color pixel : img.pixels){
            assertTrue(pixel.r >= 0 && pixel.r <= 1);
            assertTrue(pixel.g >= 0 && pixel.g <= 1);
            assertTrue(pixel.b >= 0 && pixel.b <= 1);
        }

    }
}

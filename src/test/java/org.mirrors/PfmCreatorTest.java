package org.mirrors;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.nio.ByteOrder.BIG_ENDIAN;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

class PfmCreatorTest {
    @Test
    void parse_endianness() throws InvalidPfmFileFormatException {
        assertSame(PfmCreator.parseEndianness(("1.0")), BIG_ENDIAN);
        assertSame(PfmCreator.parseEndianness(("-1.0")), LITTLE_ENDIAN);

        try {
            PfmCreator.parseEndianness("0.0");
            fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormatException e) {
            // Exception was thrown as expected
        }

        try {
            PfmCreator.parseEndianness("abc");
            fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormatException e) {
            // Exception was thrown as expected
        }
    }

    @Test
    void parse_img_size() throws InvalidPfmFileFormatException {

        int[] expectedSize = {3, 2};
        int[] actualSize = PfmCreator.parseImgSize("3 2");
        assertArrayEquals(expectedSize, actualSize);

        try {
            PfmCreator.parseImgSize("-1 3");
            fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormatException e) {
            // Exception was thrown as expected
        }

        try {
            PfmCreator.parseImgSize("3 2 1");
            fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormatException e) {
            // Exception was thrown as expected
        }
    }

    @Test
    void read_pfm_image() throws IOException, InvalidPfmFileFormatException {
        byte[][] bytes_arrays = new byte[2][];
        bytes_arrays[0] = Global.LE_ReferenceBytes;
        bytes_arrays[1] = Global.BE_ReferenceBytes;
        for (byte [] bytes_array : bytes_arrays){
            HDRImage img = PfmCreator.read_pfm_image(new ByteArrayInputStream(bytes_array));
            assertEquals(3, img.width);
            assertEquals(2, img.height);

            assertTrue(img.getPixel(0, 0).isClose(new Color(1.0e1f, 2.0e1f, 3.0e1f)));
            assertTrue(img.getPixel(1, 0).isClose(new Color(4.0e1f, 5.0e1f, 6.0e1f)));
            assertTrue(img.getPixel(2, 0).isClose(new Color(7.0e1f, 8.0e1f, 9.0e1f)));
            assertTrue(img.getPixel(0, 1).isClose(new Color(1.0e2f, 2.0e2f, 3.0e2f)));
            assertTrue(img.getPixel(0, 0).isClose(new Color(1.0e1f, 2.0e1f, 3.0e1f)));
            assertTrue(img.getPixel(1, 1).isClose(new Color(4.0e2f, 5.0e2f, 6.0e2f)));
            assertTrue(img.getPixel(2, 1).isClose(new Color(7.0e2f, 8.0e2f, 9.0e2f)));
        }
    }

    @Test
    void read_pfm_image_wrong() throws IOException {
        try {
            PfmCreator.read_pfm_image(new ByteArrayInputStream("PF\n3 2\n-1.0\nstop".getBytes()));
            fail("Expected InvalidPfmFileFormat was not thrown");
        } catch (InvalidPfmFileFormatException e) {
            // Exception was thrown as expected
        }
    }
}
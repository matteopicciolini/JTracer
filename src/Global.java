import java.util.function.Supplier;
/**
 * This class contains the te hexadecimal data array present in the files
 * reference_be and reference_le written in byte.
 * LE array is written with little endian order whereas the BE array is inverted cause of using big endian order.
 */
public class Global {
    public static byte[] LE_reference_bytes;
    public static byte[] BE_reference_bytes;

    static {
        LE_reference_bytes = new byte[]{
                (byte) 0x50, (byte) 0x46, (byte) 0x0a, (byte) 0x33, (byte) 0x20, (byte) 0x32,
                (byte) 0x0a, (byte) 0x2d, (byte) 0x31, (byte) 0x2e, (byte) 0x30, (byte) 0x0a,
                (byte) 0x00, (byte) 0x00, (byte) 0xc8, (byte) 0x42, (byte) 0x00, (byte) 0x00,
                (byte) 0x48, (byte) 0x43, (byte) 0x00, (byte) 0x00, (byte) 0x96, (byte) 0x43,
                (byte) 0x00, (byte) 0x00, (byte) 0xc8, (byte) 0x43, (byte) 0x00, (byte) 0x00,
                (byte) 0xfa, (byte) 0x43, (byte) 0x00, (byte) 0x00, (byte) 0x16, (byte) 0x44,
                (byte) 0x00, (byte) 0x00, (byte) 0x2f, (byte) 0x44, (byte) 0x00, (byte) 0x00,
                (byte) 0x48, (byte) 0x44, (byte) 0x00, (byte) 0x00, (byte) 0x61, (byte) 0x44,
                (byte) 0x00, (byte) 0x00, (byte) 0x20, (byte) 0x41, (byte) 0x00, (byte) 0x00,
                (byte) 0xa0, (byte) 0x41, (byte) 0x00, (byte) 0x00, (byte) 0xf0, (byte) 0x41,
                (byte) 0x00, (byte) 0x00, (byte) 0x20, (byte) 0x42, (byte) 0x00, (byte) 0x00,
                (byte) 0x48, (byte) 0x42, (byte) 0x00, (byte) 0x00, (byte) 0x70, (byte) 0x42,
                (byte) 0x00, (byte) 0x00, (byte) 0x8c, (byte) 0x42, (byte) 0x00, (byte) 0x00,
                (byte) 0xa0, (byte) 0x42, (byte) 0x00, (byte) 0x00, (byte) 0xb4, (byte) 0x42};

        BE_reference_bytes = new byte[]{
                (byte) 0x50, (byte) 0x46, (byte) 0x0A, (byte) 0x33, (byte) 0x20, (byte) 0x32,
                (byte) 0x0A, (byte) 0x31, (byte) 0x2E, (byte) 0x30, (byte) 0x0A, (byte) 0x42,
                (byte) 0xC8, (byte) 0x00, (byte) 0x00, (byte) 0x43, (byte) 0x48, (byte) 0x00,
                (byte) 0x00, (byte) 0x43, (byte) 0x96, (byte) 0x00, (byte) 0x00, (byte) 0x43,
                (byte) 0xC8, (byte) 0x00, (byte) 0x00, (byte) 0x43, (byte) 0xFA, (byte) 0x00,
                (byte) 0x00, (byte) 0x44, (byte) 0x16, (byte) 0x00, (byte) 0x00, (byte) 0x44,
                (byte) 0x2F, (byte) 0x00, (byte) 0x00, (byte) 0x44, (byte) 0x48, (byte) 0x00,
                (byte) 0x00, (byte) 0x44, (byte) 0x61, (byte) 0x00, (byte) 0x00, (byte) 0x41,
                (byte) 0x20, (byte) 0x00, (byte) 0x00, (byte) 0x41, (byte) 0xA0, (byte) 0x00,
                (byte) 0x00, (byte) 0x41, (byte) 0xF0, (byte) 0x00, (byte) 0x00, (byte) 0x42,
                (byte) 0x20, (byte) 0x00, (byte) 0x00, (byte) 0x42, (byte) 0x48, (byte) 0x00,
                (byte) 0x00, (byte) 0x42, (byte) 0x70, (byte) 0x00, (byte) 0x00, (byte) 0x42,
                (byte) 0x8C, (byte) 0x00, (byte) 0x00, (byte) 0x42, (byte) 0xA0, (byte) 0x00,
                (byte) 0x00, (byte) 0x42, (byte) 0xB4, (byte) 0x00, (byte) 0x00};
    }
}
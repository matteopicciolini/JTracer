package org.mirrors;

/**
 * This class contains the te hexadecimal data array present in the files
 * reference_be and reference_le written in byte.
 * LE array is written with little endian order whereas the BE array is inverted cause of using big endian order.
 */
public record Global() {
    public static byte[] LEReferenceBytes;
    public static byte[] BEReferenceBytes;
    public static float[] IdentityMatrix;

    public static Vec VecX;
    public static Vec VecY;
    public static Vec VecZ;
    public static Vec InvVecZ;
    public static Vec InvVecX;
    public static Color White;
    public static Color Black;


    static {
        LEReferenceBytes = new byte[]{
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

        BEReferenceBytes = new byte[]{
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

        IdentityMatrix = new float[]{
                1.0f, 0.0f, 0.0f, 0.0f,
                0.0f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f
        };

        VecX = new Vec(1.f, 0.f, 0.f);
        VecY = new Vec(0.f, 1.f, 0.f);
        VecZ = new Vec(0.f, 0.f, 1.f);
        InvVecZ = new Vec(0.f, 0.f, -1.f);
        InvVecX = new Vec(-1.f, 0.f, 0.f);
        White = new Color(0.f, 0.f, 0.f);
        Black = new Color(1.f, 1.f, 1.f);
    }

}
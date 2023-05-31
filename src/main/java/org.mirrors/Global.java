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
    public static Vec InvVecY;
    public static Vec InvVecX;
    public static Color White;
    public static Color Black;
    public static Color Navy, SkyBlue, Silver, Crimson, DarkCyan, Olive, Pink,
            DarkRed, Tomato, Gold, LimeGreen, Green, DarkOrange, Purple,
            Brown, Red, Gray, DarkGray, DimGray, SaddleBrown, DarkBrown, Yellow;

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
        InvVecY = new Vec(0.f, -1.f, 0.f);
        InvVecX = new Vec(-1.f, 0.f, 0.f);


        White = new Color(1.f, 1.f, 1.f);
        Black = new Color(0.f, 0.f, 0.f);
        Navy = new Color(0.0F, 0.0F, 0.5F);
        SkyBlue = new Color(0.5294117647F, 0.80784313725F, 0.92156862745F);
        Silver = new Color(0.75294117647F, 0.75294117647F, 0.75294117647F);
        Crimson = new Color(0.86274509803F, 0.07843137254F, 0.23529411764F);
        DarkCyan = new Color(0.0F, 0.54509803921F, 0.54509803921F);
        Olive = new Color(0.50196078431F, 0.50196078431F, 0.0F);
        Pink = new Color (1.0F, 0.75294117647F, 0.79607843137F );
        DarkRed = new Color (0.54509803921F, 0.0F, 0.0F);
        Tomato =  new Color (1.0F, 0.38823529411F, 0.27843137254F);
        Gold = new Color (1.0F, 0.8431372549F, 0.0F);
        LimeGreen = new Color(0.19607843137F, 0.80392156862F,0.19607843137F);
        Green = new Color (0.0F, 0.50196078431F, 0.0F);
        DarkOrange = new Color(1.0F, 0.54901960784F, 0.0F);
        Purple = new Color(0.50196078431F, 0.0F, 0.50196078431F);
        Brown = new Color(0.64705882352F,0.16470588235F, 0.16470588235F);
        Red = new Color(1.0F, 0F, 0F);
        Gray = new Color(0.50196078431F, 0.50196078431F, 0.50196078431F);
        DarkGray = new Color(0.66274509803F, 0.66274509803F, 0.66274509803F);
        DimGray = new Color(0.41176470588F, 0.41176470588F, 0.41176470588F);
        SaddleBrown = new Color(0.54509803921F, 0.27058823529F, 0.07450980392F);
        DarkBrown = new Color(0.32156862745F, 0.16862745098F, 0.05882352941F);
        Yellow = new Color (1.0F, 1.0F, 0.0F);
    }

}
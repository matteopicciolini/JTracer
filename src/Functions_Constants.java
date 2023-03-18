import java.io.ByteArrayOutputStream;

public class Functions_Constants {
    public static byte[] LE_reference_bytes = {
            0x50, 0x46, 0x0a, 0x33, 0x20, 0x32, 0x0a, 0x2d, 0x31, 0x2e, 0x30, 0x0a,
            0x00, 0x00, (byte) 0xc8, 0x42, 0x00, 0x00, 0x48, 0x43, 0x00, 0x00, (byte) 0x96, 0x43,
            0x00, 0x00, (byte) 0xc8, 0x43, 0x00, 0x00, (byte) 0xfa, 0x43, 0x00, 0x00, 0x16, 0x44,
            0x00, 0x00, 0x2f, 0x44, 0x00, 0x00, 0x48, 0x44, 0x00, 0x00, 0x61, 0x44,
            0x00, 0x00, 0x20, 0x41, 0x00, 0x00, (byte) 0xa0, 0x41, 0x00, 0x00, (byte) 0xf0, 0x41,
            0x00, 0x00, 0x20, 0x42, 0x00, 0x00, 0x48, 0x42, 0x00, 0x00, 0x70, 0x42,
            0x00, 0x00, (byte) 0x8c, 0x42, 0x00, 0x00, (byte) 0xa0, 0x42, 0x00, 0x00, (byte) 0xb4, 0x42};

    public static byte[] BE_reference_bytes = {
            0x50, 0x46, 0x0A, 0x33, 0x20, 0x32, 0x0A, 0x31,
            0x2E, 0x30, 0x0A, 0x42, (byte) 0xC8, 0x00, 0x00, 0x43,
            0x48, 0x00, 0x00, 0x43, (byte) 0x96, 0x00, 0x00, 0x43,
            (byte) 0xC8, 0x00, 0x00, 0x43, (byte) 0xFA, 0x00, 0x00, 0x44,
            0x16, 0x00, 0x00, 0x44, 0x2F, 0x00, 0x00, 0x44,
            0x48, 0x00, 0x00, 0x44, 0x61, 0x00, 0x00, 0x41,
            0x20, 0x00, 0x00, 0x41, (byte) 0xA0, 0x00, 0x00, 0x41,
            (byte) 0xF0, 0x00, 0x00, 0x42, 0x20, 0x00, 0x00, 0x42,
            0x48, 0x00, 0x00, 0x42, 0x70, 0x00, 0x00, 0x42,
            (byte) 0x8C, 0x00, 0x00, 0x42, (byte) 0xA0, 0x00, 0x00, 0x42,
            (byte) 0xB4, 0x00, 0x00};

    public static void byte_reverse(byte[] a, int n){
        byte t;
        for(int i = 0; i < n / 2; ++i){
            t = a[i];
            a[i] = a[n - i - 1];
            a[n - i - 1] = t;
        }
    }

    public static boolean match(ByteArrayOutputStream stream, byte[] reference_bytes){
        int i = 0;
        boolean match = true;
            if (stream.size() != reference_bytes.length) {
                match = false;
            } else {
                if (match) for (byte b : reference_bytes) {
                    if (b == stream.toByteArray()[i]) i++;
                    else {
                        match = false;
                        break;
                    }
                }
            }
        return match;
    }


}

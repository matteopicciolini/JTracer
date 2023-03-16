import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Functions_ConstantsTest {

    @Test
    void reverse() {
        byte[] bytes = {0x50, 0x46, 0x0a, 0x33};
        Functions_Constants.byte_reverse(bytes, bytes.length);
        byte[] bytes_reversed = {0x33, 0x0a, 0x46, 0x50};
        for (int i = 0; i < bytes.length; ++i) {
            assertTrue(bytes_reversed[i] == bytes[i]);
        }
    }
}
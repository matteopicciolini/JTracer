import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GeometryTest {

    @Test
    void createInstance() {
    }

    @Test
    void difference() {
    }

    @Test
    void sum() {
    }

    @Test
    void testToString() {
    }

    @Test
    void is_close() {
        Vec a = new Vec(1.0f, 2.0f, 3.0f);
        Vec b = new Vec(4.0f, 6.0f, 8.0f);
        assertTrue(a.is_close(a));
        assertFalse(a.is_close(b));
    }
}
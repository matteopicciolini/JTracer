import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GeometryTest {
    @Test
    void isClose() {
        Vec a = new Vec(1.0f, 2.0f, 3.0f);
        Vec b = new Vec(4.0f, 6.0f, 8.0f);
        assertTrue(a.isClose(a));
        assertFalse(a.isClose(b));

        Point c = new Point(1.0f, 2.0f, 3.0f);
        Point d = new Point(4.0f, 6.0f, 8.0f);
        assertTrue(c.isClose(c));
        assertFalse(c.isClose(d));
    }
}
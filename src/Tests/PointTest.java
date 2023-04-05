import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    @Test
    void pointOperations() {
        Point p1 = new Point(1.0f, 2.0f, 3.0f);
        Point p2 = new Point(4.0f, 6.0f, 8.0f);
        Vec v = new Vec(4.0f, 6.0f, 8.0f);

        assertTrue(p1.dot(2.0f).isClose(new Point(2.0f, 4.0f, 6.0f)));
        assertTrue(p1.sum(v).isClose(new Point(5.0f, 8.0f, 11.0f)));
        assertTrue(p2.minus(p1).isClose(new Vec(3.0f, 4.0f, 5.0f)));
        assertTrue(p1.minus(v).isClose(new Point(-3.0f, -4.0f, -5.0f)));
    }

}
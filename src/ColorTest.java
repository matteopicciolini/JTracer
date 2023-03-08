import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
class ColorTest {


    void testEquals() {
    }

    @org.junit.jupiter.api.Test
    void sum() {
        Color color1 = new Color(1.0f, 3.0f, 5.0f);
        Color color2 = new Color(2.0f, 4.0f, 6.0f);
        Color somma = new Color(3f, 7f, 21f);
        assertTrue(somma.equals(color1.sum(color2)));
    }
}
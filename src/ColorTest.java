import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @org.junit.jupiter.api.Test
    void prod() {
        Color color1 = new Color(1.0f, 2.0f, 3.0f);
        Color color2 = new Color(2.0f, 3.0f, 4.0f);
        Color color_prod = color1.prod(color2);
        Color color_exact = new Color(2.0f, 6.0f, 12.0f);
    }
}
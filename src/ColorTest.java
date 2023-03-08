import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertTrue;

class ColorTest {

    @org.junit.jupiter.api.Test
    void sum() {
        Color color1 = new Color(1.0f, 3.0f, 5.0f);
        Color color2 = new Color(2.0f, 4.0f, 6.0f);
        Color somma = new Color(3f, 7f, 21f);
        assertTrue(somma.is_close(color1.sum(color2)));
    }
    @Test
    void prod() {
        Color color = new Color(1.0f, 2.0f, 3.0f);
        float scalar = 2.0f;
        Color color_prod = color.prod(scalar);
        Color color_prod_exact = new Color(2.0f, 4.0f, 6.0f);
        assertTrue(color_prod_exact.is_close(color_prod));
    }

        @Test
    void testProd () {
        Color color1 = new Color(1.0f, 2.0f, 3.0f);
        Color color2 = new Color(2.0f, 3.0f, 4.0f);
        Color color_prod = color1.prod(color2);
        Color color_exact = new Color(2.0f, 6.0f, 12.0f);
        assertTrue(color_prod.is_close(color_exact));
    }

    @Test
    void difference () {
        Color color1 = new Color(1.0f, 2.0f, 3.0f);
        Color color2 = new Color(2.0f, 3.0f, 4.0f);
        Color color_diff = color1.difference(color2);
        Color color_diff_exact = new Color(1.0f, 1.0f, 1.0f);
        assertTrue(color_diff.is_close(color_diff_exact));
    }

    @Test
    void is_close () {
        Color color1 = new Color(1.0f, 2.0f, 3.0f);
        Color color2 = new Color(2.0f, 3.0f, 4.0f);
        assertTrue(false == color1.is_close(color2));
    }
}

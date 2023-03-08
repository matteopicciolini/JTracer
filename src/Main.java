
public class Main {
    public static void main(String[] args) {

        Color color1 = new Color(1111.0f, 3.0f, 5.0f);
        Color color2 = new Color(2.0f, 4.0f, 6.0f);
        Color color3 = color1.sum(color2);
        System.out.println(color3.r + color3.r + color3.r);
        System.out.println(color1.is_close(color2));



    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Color colore = new Color(-3.0f,2.0f,1.0f);
        Color colore2 = colore.prod(colore);
        System.out.println(colore2.r);
    }
}

public class Main {
    public static void main(String[] args) {
        //making some tries of Color class
        Color color1 = new Color(1.0f, 3.0f, 5.0f);
        Color color2 = new Color(2.0f, 4.0f, 6.0f);
        Color color3 = color1.sum(color2);
        //System.out.println(color3.r);

        //Let's use the HDR class and build some pixels
        int height = 1080;
        int width = 1920;
        HDR_Image hdr = new HDR_Image(width, height);
        hdr.set_pixel(3, 7, color1);
        System.out.println(hdr.get_pixel(3, 7).toString());
        Color incr = new Color(1.0f, 1.0f, 1.0f);
        for (int i = 0; i < 100; ++i) {
            color1 = color1.sum(incr);
            hdr.set_pixel(i, i, color1);
            System.out.println(hdr.get_pixel(i, i).toString());
        }
    }
}
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
public class random_image {

    public static void main(String[] args) throws IOException {


        HDR_Image img=new HDR_Image(200, 300);
        Random rand = new Random();
        float upperbound = 255f;
        for (int x=0; x<200; x++) {
            for (int y = 0; y < 300; y++) {
                Color color1 = new Color(rand.nextFloat(upperbound), rand.nextFloat(upperbound), rand.nextFloat(upperbound));
                img.set_pixel(x, y, color1);
            }
        }
        OutputStream out=new FileOutputStream("rand_img.png");
        img.normalize_image(0.18f);
        img.clamp_image();
        img.write_ldr_image(out, "PNG",2.20f);
            }
}

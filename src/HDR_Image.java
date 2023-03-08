import static org.junit.Assert.assertTrue;
public class HDR_Image {
    public float image[];
    public int height;
    public int width;
    public Color pixels[];
    public HDR_Image() {
        this.width=0;
        this.height=0;
        this.image = new float[this.width * this.height];
        this.pixels=new Color[this.width * this.height];
    }

    public HDR_Image(int width, int height) {
        this.width=width;
        this.height=height;
        this.image = new float[this.width * this.height];
        this.pixels=new Color[this.width * this.height];


    }
    public Color get_pixel(int x, int y) {
        assertTrue((x >= 0) &&(x < this.width));
        assertTrue((y >= 0) &&(y < this.height));
        return this.pixels[y * this.width + x];
    }

    public void set_pixels(int x, int y, Color color) {
        assertTrue((x >= 0) &&(x < this.width));
        assertTrue((y >= 0) &&(y < this.height));
        this.pixels[y * this.width + x]=color;
    }
}
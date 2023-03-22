/**
 * Class used to set the color of a single pixel of an image.
 * It contains many methods as sum, product and scalar product between two pixels RGB triad.
 */
public class Color {

    public float r;
    public float g;
    public float b;

    public Color(){
        this.r = 0.0F;
        this.g = 0.0F;
        this.b = 0.0F;
    }
    public Color(float r, float g, float b){
        if (r >= 0 && g >= 0 && b >= 0){
            this.r = r;
            this.g = g;
            this.b = b;
        }
        else{
            System.out.println("Error: Class Color can't have negative parameters.");
            System.exit(-1);
        }
    }
    public Color sum(Color color1){
        Color color = new Color();
        color.r = this.r + color1.r;
        color.g = this.g + color1.g;
        color.b = this.b + color1.b;
        return color;
    }

    public Color prod(float scalar){
        Color color = new Color();
        color.r = this.r * scalar;
        color.g = this.g * scalar;
        color.b = this.b * scalar;
        return color;
    }

    public Color prod(Color color1) {
        Color color = new Color();
        color.r = this.r * color1.r;
        color.g = this.g * color1.g;
        color.b = this.b * color1.b;
        return color;
    }
    public Color difference(Color color1){
        Color color = new Color();
        color.r = Math.abs(this.r - color1.r);
        color.g = Math.abs(this.g - color1.g);
        color.b = Math.abs(this.b - color1.b);
        return color;
    }
    public boolean is_close(Color color1){
        float epsilon = 1e-5F;
        Color color_diff = this.difference(color1);
        return (color_diff.r < epsilon &&
                color_diff.g < epsilon &&
                color_diff.b < epsilon);
    }

    @Override
    public String toString() {
        return "<r: " + r + ", g: " + g + ", b: " + b + ">";
    }

    public float luminosity(){


        return ((Math.max(Math.max(this.r, this.g ), this.b))+Math.min(Math.min(this.r, this.g), this.b))/2.0f;
    }
}
public class Color {

    public float r;
    public float g;
    public float b;
    public Color() {
        this.r=0.0f;
        this.g=0.0f;
        this.b=0.0f;
    }

    public Color(float r, float g, float b) {
        this.r=r;
        this.g=b;
        this.b=g;

        if (r<0f || b<0f || g<0f || r>= 255f || g>= 255f || b>= 255f){
            System.out.println("Inserito un parametro negativo o maggiore di 255");
            System.exit(-1);

            }
        }

    public Color sum(Color color2){
        Color result = new Color();
        result.r=this.r+color2.r;
        result.g=this.g+color2.g;
        result.b=this.b+color2.b;
        return result;
    }

    public boolean equals(Color b){
        float epsilon = 1e-5f;
        Color result = new Color();
        result.r=this.r-b.r;
        result.g=this.g-b.g;
        result.b=this.b-b.b;
        if(result.r<epsilon && result.b<epsilon && result.g<epsilon) {
            return true;
        }
        else {
            return false;
        }

        }
    }



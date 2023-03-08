class Color {
    public float r;
    public float g;
    public float b;

    public Color(){
        this.r = 0.0F;
        this.g = 0.0F;
        this.b = 0.0F;
    }
    public Color(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color prod(float scalar){
        Color color = new Color();
        color.r = this.r * scalar;
        color.g = this.g * scalar;
        color.b = this.b * scalar;
        return color;
    }

    public Color prod(Color color1){
        Color color = new Color();
        color.r = this.r * color1.r;
        color.g = this.g * color1.g;
        color.b = this.b * color1.b;
        return color;
}

public class Transformation {
    public float[] m;
    public float[] m_i;


    public Transformation(){
        m= new float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
        m_i=new float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    }
    public Transformation(float[] m){
        this.m=m;
        m_i=null;
    }
}

public class Trasformation {
    public float[] m;
    public float[] m_i;


    public Trasformation(){
        m= new float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
        m_i=new float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    }
    public Trasformation(float[] m){
        this.m=m;
        m_i=null;
    }
}

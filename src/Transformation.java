public class Transformation {
    public float[] m;
    public float[] m_i;


    public Transformation() {
        m = new float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
        m_i = new float[]{1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1f};
    }

    public Transformation(float[] m, float[] m_i) {
        this.m = m;
        this.m_i = m_i;
    }
}
    /*
    public Transformation matr_prod(float[] m1, float[] m2_i){
        Float[] m3=new Float[m1.length];
        for (int i = 4 - 1; i >= 0 ; --i) {
            for (int j = 0; j < 4; ++j) {
                r = readFloat(stream, endianness);
                g = readFloat(stream, endianness);
                b = readFloat(stream, endianness);
                result.set_pixel(j, i, new Color(r, g, b));
            }
        }
        return result;
    }

        return m3;
    }
}*/

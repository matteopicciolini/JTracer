package AbstractClasses;
import java.lang.reflect.InvocationTargetException;
import static java.lang.Math.abs;

public abstract class Geometry{
    public float x;
    public float y;
    public float z;

    public Geometry(){
        this.x = 0.f;
        this.y = 0.f;
        this.z = 0.f;
    }
    public Geometry(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    protected abstract Geometry createInstance(float x, float y, float z);

    protected static <T extends Geometry> T difference(Geometry a, Geometry b, Class<T> returnType) {
        try {
            T result = returnType.getDeclaredConstructor().newInstance();
            result.x = a.x - b.x;
            result.y = a.y - b.y;
            result.z = a.z - b.z;
            return result;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    protected static <T extends Geometry> T sum(Geometry a, Geometry b, Class<T> returnType) {
        try {
            T result = returnType.getDeclaredConstructor().newInstance();
            result.x = a.x + b.x;
            result.y = a.y + b.y;
            result.z = a.z + b.z;
            return result;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {return "%s(%s, %s, %s)".formatted(this.getClass().getName(), x, y, z);}

    public boolean is_close(Geometry other) {
        if (other.getClass() != this.getClass()){throw new IllegalArgumentException("Cannot compare different types");}
        float epsilon = 1e-5F;
        Geometry diff = difference(this, other, this.getClass());
        assert diff != null;
        return (abs(diff.x) < epsilon &&
                abs(diff.y) < epsilon &&
                abs(diff.z) < epsilon);
    }
}





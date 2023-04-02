package AbstractClasses;
import java.lang.reflect.InvocationTargetException;
import static java.lang.Math.sqrt;

public abstract class OrientedObject extends Geometry {
    public OrientedObject() {
        super();
    }
    public OrientedObject(float x, float y, float z) {
        super(x, y, z);
    }

    public Geometry neg() {
        return (createInstance(-this.x, -this.y, -this.z));
    }

    public float dot(OrientedObject other) {
        return (this.x * other.x +
                this.y * other.y +
                this.z * other.z
        );
    }

    public float norm(){
        return (float) sqrt(this.squared_norm());
    }
    public float squared_norm(){
        return this.dot(this);
    }

    public void normalize(){
        float norm = this.norm();
        this.x /= norm;
        this.y /= norm;
        this.z /= norm;
    }

    protected static <T extends OrientedObject> T cross(OrientedObject a, OrientedObject b, Class<T> returnType) {
        try {
            T result = returnType.getDeclaredConstructor().newInstance();
            result.x = a.y * b.z - a.z * b.y;
            result.y = a.z * b.x - a.x * b.z;
            result.z = a.x * b.y - a.y * b.x;
            return result;
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}


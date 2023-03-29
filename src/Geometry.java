import java.util.function.Supplier;

public interface Geometry {
    float get_x();
    float get_y();
    float get_z();

    void set_x(float x);
    void set_y(float y);
    void set_z(float z);

    static <T extends Geometry, F extends Geometry, R extends Geometry> R difference(F template,
                                                                                            T template_to_compare,
                                                                                            Supplier<R> constructor) {
        R diff = constructor.get();
        diff.set_x(Math.abs(template.get_x() - template_to_compare.get_x()));
        diff.set_y(Math.abs(template.get_y() - template_to_compare.get_y()));
        diff.set_z(Math.abs(template.get_z() - template_to_compare.get_z()));
        return diff;
    }

    static <T extends Geometry, F extends Geometry, R extends Geometry> R sum(F template,
                                                                                     T template_to_compare,
                                                                                     Supplier<R> constructor) {
        R sum = constructor.get();
        sum.set_x(template.get_x() + template_to_compare.get_x());
        sum.set_y(template.get_y() + template_to_compare.get_y());
        sum.set_z(template.get_z() + template_to_compare.get_z());
        return sum;
    }
}

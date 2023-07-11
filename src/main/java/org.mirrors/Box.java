package org.mirrors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mirrors.Global.*;
import java.util.Arrays;
public class Box extends Shape {
    public Point min;
    public Point max;

    public Box(Point min, Point max, Transformation transformation, Material material) {
        super(transformation, material);
        this.min = min;
        this.max = max;
        checkMinMax();
    }

    public Box() {
        this(new Point(-0.5f, -0.5f, -0.5f), new Point(0.5f, 0.5f, 0.5f), new Transformation(), new Material());
    }
    private void checkMinMax() {
        for (int i = 0; i < 3; i++) {
            if (min.get(i) > max.get(i)) {
                System.out.println(
                        "\u001B[31mWarning: Box has no consistent minimum and maximum vertices (component " + i + ")." +
                                " Default values will be used\u001B[0m"
                );
                min = new Point(-0.5f, -0.5f, -0.5f);
                max = new Point(0.5f, 0.5f, 0.5f);
                break;
            }
        }
    }

    public boolean isPointInternal(Point point) {
        point = (Point) this.transformation.inverse().times(point);
        return point.x >= min.x && point.x <= max.x &&
                point.y >= min.y && point.y <= max.y &&
                point.z >= min.z && point.z <= max.z;
    }
    @Override
    public HitRecord rayIntersection(Ray ray) {
        Ray iray = this.transformation.inverse().times(ray);
        float t1 = iray.tMin;
        float t2 = iray.tMax;
        int minDir = -1;
        int maxDir = -1;

        for (int i = 0; i < 3; i++) {
            float tMin = (min.get(i) - iray.origin.get(i)) / iray.dir.get(i);
            float tMax = (max.get(i) - iray.origin.get(i)) / iray.dir.get(i);

            if (tMin > tMax) {
                float t = tMin;
                tMin = tMax;
                tMax = t;
            }
            if (tMin > t1) {
                t1 = tMin;
                minDir = i;
            }
            if (tMax < t2) {
                t2 = tMax;
                maxDir = i;
            }

            if (t1 > t2) {
                return null;
            }
        }

        float t = t1;
        int dir = minDir;
        if (minDir == -1) {
            t = t2;
            dir = maxDir;
        }

        Point point = iray.at(t);
        Normal normal = getNormal(dir, iray.dir);
        return new HitRecord((Point) this.transformation.times(point), (Normal) this.transformation.times(normal), toSurPoint(point, normal), t, ray, this);
    }

    @Override
    public List<HitRecord> rayIntersectionList(Ray ray) {
        Ray iray = this.transformation.inverse().times(ray);
        float t1 = iray.tMin;
        float t2 = iray.tMax;
        int minDir = -1;
        int maxDir = -1;

        for (int i = 0; i < 3; i++) {
            float tMin = (min.get(i) - iray.origin.get(i)) / iray.dir.get(i);
            float tMax = (max.get(i) - iray.origin.get(i)) / iray.dir.get(i);

            if (tMin > tMax) {
                float t = tMin;
                tMin = tMax;
                tMax = t;
            }
            if (tMin > t1) {
                t1 = tMin;
                minDir = i;
            }
            if (tMax < t2) {
                t2 = tMax;
                maxDir = i;
            }

            if (t1 > t2) {
                return null;
            }
        }

        if (minDir == -1) {
            Point hit2 = iray.at(t2);
            Normal n2 = getNormal(maxDir, iray.dir);

            List<HitRecord> resultList = new ArrayList<>();
            resultList.add(new HitRecord(
                    (Point) this.transformation.times(hit2),
                    (Normal) this.transformation.times(n2),
                    toSurPoint(hit2, n2),
                    t2,
                    ray,
                    this
            ));
            return resultList;
        } else {
            Point hit1 = iray.at(t1);
            Normal n1 = getNormal(minDir, iray.dir);

            Point hit2 = iray.at(t2);
            Normal n2 = getNormal(maxDir, iray.dir);

            List<HitRecord> resultList = new ArrayList<>();
            resultList.add(new HitRecord(
                    (Point) this.transformation.times(hit1),
                    (Normal) this.transformation.times(n1),
                    toSurPoint(hit1, n1),
                    t1,
                    ray,
                    this
            ));
            resultList.add(new HitRecord(
                    (Point) this.transformation.times(hit2),
                    (Normal) this.transformation.times(n2),
                    toSurPoint(hit2, n2),
                    t2,
                    ray,
                    this
            ));
            return resultList;
        }
    }

    @Override
    public boolean isInternal(Point point) {
        Point realP = (Point) this.transformation.inverse().times(point);
        return realP.x >= min.x && realP.x <= max.x
                && realP.y >= min.y && realP.y <= max.y
                && realP.z >= min.z && realP.z <= max.z;
    }



    private Normal getNormal(int minDir, Vec rayDir) {
        Normal norm = switch (minDir) {
            case 0 -> VecX.toNormal();
            case 1 -> VecY.toNormal();
            case 2 -> VecZ.toNormal();
            default -> new Normal();
        };
        return (norm.toVec().dot(rayDir) >= 0) ? (Normal) norm.neg() : norm;
    }

    private Vec2d toSurPoint(Point hit, Normal normal) {
        int face;
        if (normal.isClose(VecX.toNormal())) {
            face = 0;
        } else if (normal.isClose(InvVecX.toNormal())) {
            face = 1;
        } else if (normal.isClose(VecY.toNormal())) {
            face = 2;
        } else if (normal.isClose(InvVecY.toNormal())) {
            face = 3;
        } else if (normal.isClose(VecZ.toNormal())) {
            face = 4;
        } else if (normal.isClose(InvVecZ.toNormal())) {
            face = 5;
        } else {
            throw new RuntimeException();
        }

        return switch (face) {
            case 0 ->
                    new Vec2d(0.50F + (max.z - hit.z) / (max.z - min.z) * 0.25F, 0.75F - (hit.y - min.y) / (max.y - min.y) * 0.25F);
            case 1 ->
                    new Vec2d((hit.z - min.z) / (max.z - min.z) * 0.25F, 0.75F - (hit.y - min.y) / (max.y - min.y) * 0.25F);
            case 2 ->
                    new Vec2d(0.25F + (hit.x - min.x) / (max.x - min.x) * 0.25F, 0.50F - (max.z - hit.z) / (max.z - min.z) * 0.25F);
            case 3 ->
                    new Vec2d(0.25F + (hit.x - min.x) / (max.x - min.x) * 0.25F, 1.00F - (hit.z - min.z) / (max.z - min.z) * 0.25F);
            case 4 ->
                    new Vec2d(0.25F + (hit.x - min.x) / (max.x - min.x) * 0.25F, 0.75F - (hit.y - min.y) / (max.y - min.y) * 0.25F);
            case 5 ->
                    new Vec2d(0.75F + (max.x - hit.x) / (max.x - min.x) * 0.25F, 0.75F - (hit.y - min.y) / (max.y - min.y) * 0.25F);
            default -> throw new RuntimeException();
        };
    }


}
package geometries;

import primitives.Point;
import primitives.Ray;
import java.util.List;

public interface Intersectable {
    List<Point> findIntersections(Ray ray);
}

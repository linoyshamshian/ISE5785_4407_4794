package primitives;

public class Point {
    public static final Point ZERO = new Point(Double3.ZERO);
    final Double3 xyz;

    public Point(double x, double y, double z) {
        xyz = new Double3(x, y, z);
    }

    public Point(Double3 xyz) {
        this.xyz = xyz;
    }

    public Vector subtract(Point p1) {
        return new Vector(xyz.subtract(p1.xyz));
    }

    public Point add(Vector v1) {
        return new Point(xyz.add(v1.xyz));
    }

    public double distanceSquared(Point p1) {
        double dx = xyz.d1() - p1.xyz.d1();
        double dy = xyz.d2() - p1.xyz.d2();
        double dz = xyz.d3() - p1.xyz.d3();
        return dx * dx + dy * dy + dz * dz;
    }

    public double distance(Point p1) {
       return Math.sqrt(distanceSquared(p1));
    }
}

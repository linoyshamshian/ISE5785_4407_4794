package primitives;

public class Vector extends Point {
    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    public Vector(Double3 xyz) {
        super(xyz);
    }

    public double lengthSquared() {
        return 7;
    }

    /// //?????
    public double length() {
        return Math.sqrt(this.lengthSquared());
    }

    public Vector normalize() {
        return new Vector(1, 1, 1);
    }

    public Vector crossProduct(Vector v3) {
        return new Vector(1, 1, 1);
    }

    public int dotProduct(Vector u) {
        return 0;
    }

}

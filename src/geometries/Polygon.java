package geometries;

import static java.lang.Double.*;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.*;

import primitives.*;

/**
 * Polygon class represents two-dimensional polygon in 3D Cartesian coordinate
 * system
 *
 * @author Dan
 */

public class Polygon extends Geometry {
    /**
     * List of polygon's vertices
     */
    protected final List<Point> vertices;
    /**
     * Associated plane in which the polygon lays
     */
    protected final Plane plane;
    /**
     * The size of the polygon - the amount of the vertices in the polygon
     */
    private final int size;

    /**
     * Polygon constructor based on vertices list. The list must be ordered by edge
     * path. The polygon must be convex.
     *
     * @param vertices list of vertices according to their order by
     *                 edge path
     * @throws IllegalArgumentException in any case of illegal combination of
     *                                  vertices:
     *                                  <ul>
     *                                  <li>Less than 3 vertices</li>
     *                                  <li>Consequent vertices are in the same
     *                                  point
     *                                  <li>The vertices are not in the same
     *                                  plane</li>
     *                                  <li>The order of vertices is not according
     *                                  to edge path</li>
     *                                  <li>Three consequent vertices lay in the
     *                                  same line (180&#176; angle between two
     *                                  consequent edges)
     *                                  <li>The polygon is concave (not convex)</li>
     *                                  </ul>
     */
    public Polygon(Point... vertices) {
        if (vertices.length < 3)
            throw new IllegalArgumentException("A polygon can't have less than 3 vertices");
        this.vertices = List.of(vertices);
        size = vertices.length;

        // Generate the plane according to the first three vertices and associate the
        // polygon with this plane.
        // The plane holds the invariant normal (orthogonal unit) vector to the polygon
        plane = new Plane(vertices[0], vertices[1], vertices[2]);
        if (size == 3) return; // no need for more tests for a Triangle

        Vector n = plane.getNormal(vertices[0]);
        // Subtracting any subsequent points will throw an IllegalArgumentException
        // because of Zero Vector if they are in the same point
        Vector edge1 = vertices[size - 1].subtract(vertices[size - 2]);
        Vector edge2 = vertices[0].subtract(vertices[size - 1]);

        // Cross Product of any subsequent edges will throw an IllegalArgumentException
        // because of Zero Vector if they connect three vertices that lay in the same
        // line.
        // Generate the direction of the polygon according to the angle between last and
        // first edge being less than 180deg. It is hold by the sign of its dot product
        // with the normal. If all the rest consequent edges will generate the same sign
        // - the polygon is convex ("kamur" in Hebrew).
        boolean positive = edge1.crossProduct(edge2).dotProduct(n) > 0;
        for (var i = 1; i < size; ++i) {
            // Test that the point is in the same plane as calculated originally
            if (!isZero(vertices[i].subtract(vertices[0]).dotProduct(n)))
                throw new IllegalArgumentException("All vertices of a polygon must lay in the same plane");
            // Test the consequent edges have
            edge1 = edge2;
            edge2 = vertices[i].subtract(vertices[i - 1]);
            if (positive != (edge1.crossProduct(edge2).dotProduct(n) > 0))
                throw new IllegalArgumentException("All vertices must be ordered and the polygon must be convex");
        }
    }

    @Override
    public Vector getNormal(Point point) {
        return plane.getNormal(point);
    }

    @Override
    public List<Point> findIntersections(Ray ray) {
        // Intersect ray with the polygon's plane.
        List<Point> planeIntersections = plane.findIntersections(ray);
        if (planeIntersections == null) {
            // No intersection with the plane, so no intersection with the polygon.
            return null;
        }

        // Get the intersection point with the plane.
        Point p = planeIntersections.get(0);

        // Get the vertices of the polygon.
        List<Point> polygonVertices = vertices;

        // Calculate the vectors from the vertices to the point.
        List<Vector> vertexVectors = new ArrayList<>();
        for (Point vertex : polygonVertices) {
            vertexVectors.add(p.subtract(vertex));
        }

        // Calculate the cross products of the edges and the vectors to the point.
        List<Vector> normals = new ArrayList<>();
        for (int i = 0; i < polygonVertices.size(); i++) {
            Point currentVertex = polygonVertices.get(i);
            Point nextVertex = polygonVertices.get((i + 1) % polygonVertices.size()); // Wrap around for the last edge
            normals.add(nextVertex.subtract(currentVertex).crossProduct(vertexVectors.get(i)));
        }

        // Check if all the cross products point in the same direction.
        for (int i = 0; i < normals.size() - 1; i++) {
            if (normals.get(i).dotProduct(normals.get(i + 1)) <= 0) {
                // The intersection point is outside the polygon.
                return null;
            }
        }

        // The intersection point is inside the polygon.
        return List.of(p);
    }


    public List<Point> findIntersectionsPyramid(Ray ray) {
        // Intersect the ray with the plane of the polygon
        List<Point> planeIntersections = plane.findIntersections(ray);
        if (planeIntersections == null) return null; // No intersection with the plane

        Point p = planeIntersections.get(0); // Intersection point with the plane
        Point p0 = ray.getHead();            // Ray origin
        Vector dir = ray.getDirection();     // Ray direction

        int n = vertices.size();
        Boolean positive = null; // Will hold the sign of the dot product (first side of the "pyramid")

        // Check if the intersection point is inside the polygon using the pyramid method
        for (int i = 0; i < n; i++) {
            Point p1 = vertices.get(i);
            Point p2 = vertices.get((i + 1) % n);

            Vector v1 = p1.subtract(p0); // Vector from ray origin to first vertex
            Vector v2 = p2.subtract(p0); // Vector from ray origin to next vertex

            Vector normal = v1.crossProduct(v2); // Create a face of the pyramid

            double dot = dir.dotProduct(normal); // Project ray direction on the face normal

            // If dot product is zero → ray is parallel to this pyramid face → considered outside
            if (isZero(dot)) return null;

            // Check sign consistency across all triangle faces
            if (positive == null) {
                positive = dot > 0;
            } else {
                if ((dot > 0) != positive) return null; // Opposite sign → intersection is outside
            }
        }

        // All signs are consistent → the intersection point is inside the polygon
        return List.of(p);
    }

}

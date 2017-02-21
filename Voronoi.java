import java.util.*;

// bounds: left, top, right, bottom.

public class Voronoi {
    public static List<Cell> evaluate(List<Point> points, double[] bounds) {
        final KdTree kdTree = new KdTree();

        for(Point point : points) {
            Cell parent = kdTree.getClosest(point);

            if(parent == null) {
                kdTree.add(new Cell(point, bounds));
            } else {
                kdTree.add(parent.split(point));
            }
        }

        return kdTree.getAll();
    }
}

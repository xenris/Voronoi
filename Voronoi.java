import java.util.*;

// bounds: left, top, right, bottom.

public class Voronoi {
    public static List<Cell> evaluate(List<Point> points, double[] bounds) {
        final KdTree kdTree = new KdTree();

        for(Point point : points) {
            Log.debug("point: " + point);
            Cell parent = kdTree.getClosest(point);

            if(parent == null) {
                kdTree.add(new Cell(point, bounds));
            } else {
                final Cell cell = new Cell(point);
                final List<Cell> neighbours = parent.getNeighbours(cell);
                boolean success = true; // XXX Temporary mitigation to a bug in split().

                for(Cell neighbour : neighbours) {
                    success = success && neighbour.split(cell);
                }

                if(success) {
                    kdTree.add(cell);
                }
            }

            if(Main.counter >= Main.steps) {
                break;
            }

            Main.counter++;
        }

        return kdTree.getAll();
    }
}

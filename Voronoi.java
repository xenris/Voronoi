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
                final Cell cell = new Cell(point);
                final Set<Cell> neighbours = parent.getNeighbours(cell);

                for(Cell neighbour : neighbours) {
                    neighbour.split(cell);
                }

                kdTree.add(cell);
            }

            if(Main.counter >= Main.steps) {
                break;
            }

            Main.counter++;
        }

        return kdTree.getAll();
    }
}

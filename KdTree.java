import java.util.*;

public class KdTree {
    private Node root = null;
    private final List<Cell> cells = new ArrayList<>();

    public void add(Cell cell) {
        cells.add(cell);

        if(root == null) {
            root = new Node(cell, cell.center.y);
        } else {
            Node node = root;
            int d = 1;

            while(true) {
                final int c = (cell.center.get(d) < node.split) ? 0 : 1;

                if(node.children[c] == null) {
                    node.children[c] = new Node(cell, cell.center.get(d == 0 ? 1 : 0));
                    break;
                } else {
                    node = node.children[c];
                }

                d = (d + 1) % 2;
            }
        }
    }

    public Cell getClosest(Point point) {
        if(root == null) {
            return null;
        }

        Node node = root;
        Cell best = node.cell;
        int d = 1;

        while(node != null) {
            if(closer(point, node.cell.center, best.center)) {
                best = node.cell;
            }

            final int c = (point.get(d) < node.split) ? 0 : 1;

            node = node.children[c];

            d = (d + 1) % 2;
        }

        return best;
    }

    private boolean closer(Point point, Point a, Point b) {
        final double dax = a.x - point.x;
        final double day = a.y - point.y;
        final double dbx = b.x - point.x;
        final double dby = b.y - point.y;

        double da = dax * dax + day * day;
        double db = dbx * dbx + dby * dby;

        return da < db;
    }

    public List<Cell> getAll() {
        return cells;
    }

    private static class Node {
        public Cell cell;
        public double split;
        public Node[] children = new Node[2];

        public Node(Cell cell, double split) {
            this.cell = cell;
            this.split = split;
        }
    }
}

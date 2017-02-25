import java.util.*;

public class KdTree {
    private Node root = null;
    private final List<Cell> cells = new ArrayList<>();

    public void add(Cell cell) {
        cells.add(cell);

        int d = 0;

        if(root == null) {
            root = new Node(cell, cell.center.get(d));
        } else {
            Node node = root;

            while(true) {
                final int c = (cell.center.get(d) < node.split) ? 0 : 1;

                d = (d + 1) % 2;

                if(node.children[c] == null) {
                    node.children[c] = new Node(cell, cell.center.get(d));
                    break;
                } else {
                    node = node.children[c];
                }
            }
        }
    }

    public Cell getClosest(Point point) {
        if(root == null) {
            return null;
        }

        final Node closest = root.getClosest(point, root, 0);

        return closest.cell;
    }

    private static boolean closer(Point point, Point a, Point b) {
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

        public Node getClosest(Point point, Node best, int d) {
            final boolean side = point.get(d) < split;
            final Node childA = children[side ? 0 : 1];
            final Node childB = children[side ? 1 : 0];

            if((best != this) && closer(point, this.cell.center, best.cell.center)) {
                best = this;
            }

            if((best == this) || (dist2(point, best.cell.center) >= dist2(point.get(d), split))) {
                if(childA != null) {
                    best = childA.getClosest(point, best, (d + 1) % 2);
                }

                if(childB != null) {
                    best = childB.getClosest(point, best, (d + 1) % 2);
                }
            } else {
                if(childA != null) {
                    best = childA.getClosest(point, best, (d + 1) % 2);
                }
            }

            return best;
        }

        private static double dist2(Point a, Point b) {
            final double dx = a.x - b.x;
            final double dy = a.y - b.y;

            return dx * dx + dy * dy;
        }

        private static double dist2(double a, double b) {
            final double dd = a - b;

            return dd * dd;
        }
    }
}

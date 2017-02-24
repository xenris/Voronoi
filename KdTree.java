import java.util.*;

public class KdTree {
    private Node root = null;
    private final List<Cell> cells = new ArrayList<>();

    static {
        final KdTree tree = new KdTree();
        final List<Cell> cells = new ArrayList<>();
        final Random random = new Random();

        for(int i = 0; i < 100; i++) {
            final Cell cell = new Cell(new Point(random.nextDouble(), random.nextDouble()));
            cells.add(cell);
            tree.add(cell);
        }

        for(int i = 0; i < 100; i++) {
            final Point point = new Point(random.nextDouble(), random.nextDouble());

            final Cell closest = tree.getClosest(point);
            final double cdx = closest.center.x - point.x;
            final double cdy = closest.center.y - point.y;
            final double cdist2 = cdx * cdx + cdy * cdy;

            for(Cell cell : cells) {
                final double dx = cell.center.x - point.x;
                final double dy = cell.center.y - point.y;
                final double dist2 = dx * dx + dy * dy;

                if(dist2 < cdist2) {
                    Log.log("KdTree failed: " + point + " " + closest.center + " " + cell.center);
                }
            }
        }
    }

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

        Cell closest = cells.get(0);
        double cdx = closest.center.x - point.x;
        double cdy = closest.center.y - point.y;
        double cdist2 = cdx * cdx + cdy * cdy;

        for(Cell cell : cells) {
            final double dx = cell.center.x - point.x;
            final double dy = cell.center.y - point.y;
            final double dist2 = dx * dx + dy * dy;

            if(dist2 < cdist2) {
                closest = cell;
                cdx = dx;
                cdy = dy;
                cdist2 = dist2;
            }
        }

        return closest;


        // Node node = root;
        // Node best = node;
        // int d = 1;

        // while(node != null) {
        //     if(closer(point, node.cell.center, best.cell.center)) {
        //         best = node;
        //     }

        //     final int c = (point.get(d) < node.split) ? 0 : 1;

        //     node = node.children[c];

        //     d = (d + 1) % 2;
        // }

        // return best.cell;
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

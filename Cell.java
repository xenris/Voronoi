import java.util.*;

public class Cell {
    public final Point center;
    public final List<Edge> edges = new ArrayList<>();

    public Cell(Point center) {
        this.center = center;
    }

    public Cell(Point center, double[] bounds) {
        this.center = center;

        final double l = bounds[0];
        final double t = bounds[1];
        final double r = bounds[2];
        final double b = bounds[3];

        edges.add(new Edge(l, b, l, t, this, null));
        edges.add(new Edge(l, t, r, t, this, null));
        edges.add(new Edge(r, t, r, b, this, null));
        edges.add(new Edge(r, b, l, b, this, null));
    }

    public Cell split(Point point) {
        final Cell cell = new Cell(point);
        final Iterator<Edge> iterator = edges.iterator();
        final Point[] intersections = new Point[2];
        int ins = 0;

        while(iterator.hasNext()) {
            final Edge edge = iterator.next();

            final Ray ray = new Ray(center, point);

            final boolean s1 = ray.sameSide(center.x, center.y, edge.ends[0], edge.ends[1]);
            final boolean s2 = ray.sameSide(center.x, center.y, edge.ends[2], edge.ends[3]);

            if(!s1 && !s2) {
                iterator.remove();

                cell.edges.add(edge);

                if(edge.cells[0] == this) {
                    edge.cells[0] = cell;
                } else {
                    edge.cells[1] = cell;
                }
            } else if(!s1 || !s2) {
                final Point intersection = ray.intersection(new Ray(edge.ends));
                intersections[ins] = intersection;
                ins++;

                final int e1 = s1 ? 2 : 0;
                final int e2 = s2 ? 1 : 3;

                final double tx = edge.ends[e1];
                final double ty = edge.ends[e2];

                edge.ends[e1] = intersection.x;
                edge.ends[e2] = intersection.y;

                final Cell neighbour = (edge.cells[0] == this) ? edge.cells[1] : edge.cells[0];

                if(neighbour == null) {
                    final Edge newEdge = new Edge(intersection.x, intersection.y, tx, ty, cell, neighbour);
                    cell.edges.add(newEdge);
                } else {
                    System.out.println("TODO: handle neighbours");
                    // TODO
                    // neighbour.split(cell);
                }
            }
        }

        final double x1 = intersections[0].x;
        final double y1 = intersections[0].y;
        final double x2 = intersections[1].x;
        final double y2 = intersections[1].y;

        final Edge newEdge = new Edge(x1, y1, x2, y2, cell, this);

        edges.add(newEdge);
        cell.edges.add(newEdge);

        return cell;
    }

    // private void split(Cell cell) {
    //     final Ray ray = new Ray(cell.center, center);

    //     for(Edge edge : edges) {
    //         final Point intersection = ray.intersection(new Ray(edge.ends));
    //     }
    // }
}

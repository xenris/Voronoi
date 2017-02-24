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

    public void split(Cell child) {
        Log.debug("split");
        final Iterator<Edge> iterator = edges.iterator();
        final Ray ray = new Ray(center, child.center);
        final List<Point> intersections = new ArrayList<>();

        while(iterator.hasNext()) {
            final Edge edge = iterator.next();

            final int sideOfParent = ray.side(center);
            final int sideOfChild = -sideOfParent;
            final int sideOfEdgeEndA = ray.side(edge.ends[0], edge.ends[1]);
            final int sideOfEdgeEndB = ray.side(edge.ends[2], edge.ends[3]);

            final boolean edgeCrossesRay = ((sideOfEdgeEndA + sideOfEdgeEndB) == 0) && (sideOfEdgeEndA != 0);
            final boolean edgeTouchesRayOnChildSide = (sideOfEdgeEndA + sideOfEdgeEndB) == sideOfChild;
            final boolean edgeTouchesRayOnParentSide = (sideOfEdgeEndA + sideOfEdgeEndB) == sideOfParent;
            final boolean edgeOnChildSide = (sideOfEdgeEndA == sideOfEdgeEndB) && (sideOfEdgeEndB == sideOfChild);
            final boolean edgeOnParentSide = (sideOfEdgeEndA == sideOfEdgeEndB) && (sideOfEdgeEndB == sideOfParent);

            if(edgeCrossesRay) {
                Log.debug("edgeCrossesRay");
                final Point intersection = ray.intersection(new Ray(edge.ends));
                final Cell neighbour = (edge.cells[0] == this) ? edge.cells[1] : edge.cells[0];
                final int e1 = (sideOfEdgeEndA == sideOfChild) ? 0 : 2;
                final int e2 = (sideOfEdgeEndA == sideOfChild) ? 1 : 3;
                final double tx = edge.ends[e1];
                final double ty = edge.ends[e2];

                edge.ends[e1] = intersection.x;
                edge.ends[e2] = intersection.y;

                intersections.add(intersection);

                if(neighbour == null) {
                    final Edge e = new Edge(tx, ty, intersection.x, intersection.y, child, null);
                    child.edges.add(e);
                }
            } else if(edgeOnChildSide || edgeTouchesRayOnChildSide) {
                Log.debug("edgeOnChildSide || edgeTouchesRayOnChildSide");
                final Cell neighbour = (edge.cells[0] == this) ? edge.cells[1] : edge.cells[0];

                iterator.remove();

                if(neighbour != null) {
                    neighbour.edges.remove(edge);
                } else {
                    child.edges.add(edge);
                    edge.cells[0] = child;
                    edge.cells[1] = null;
                }
            } else if(edgeOnParentSide) {
                Log.debug("edgeOnParentSide");
                // Ignore.
            } else if(edgeTouchesRayOnParentSide) {
                Log.debug("edgeTouchesRayOnParentSide");
                final Point intersection = ray.intersection(new Ray(edge.ends));
                intersections.add(intersection);
            }
        }

        if(intersections.size() == 2) {
            final Point i1 = intersections.get(0);
            final Point i2 = intersections.get(1);
            final Edge e = new Edge(i1.x, i1.y, i2.x, i2.y, this, child);

            edges.add(e);
            child.edges.add(e);
        } else {
            Log.error("Found " + intersections.size() + " intersections while spliting");
        }
    }

    public Set<Cell> getNeighbours(Cell child) {
        Log.debug("getNeighbours(Cell)");

        final Set<Cell> neighbours = new HashSet<>();

        neighbours.add(this);

        getNeighbours(child, neighbours);

        return neighbours;
    }

    private void getNeighbours(Cell child, Set<Cell> neighbours) {
        final Ray ray = new Ray(child.center, center);

        final int sideOfParent = ray.side(center);
        final int sideOfChild = -sideOfParent;

        for(Edge edge : edges) {
            Log.debug("edge: " + edge);
            final int sideOfEdgeEndA = ray.side(edge.ends[0], edge.ends[1]);
            final int sideOfEdgeEndB = ray.side(edge.ends[2], edge.ends[3]);

            final boolean edgeCrossesRay = ((sideOfEdgeEndA + sideOfEdgeEndB) == 0) && (sideOfEdgeEndA != 0);
            final boolean edgeTouchesRayOnChildSide = (sideOfEdgeEndA + sideOfEdgeEndB) == sideOfChild;
            final boolean edgeOnChildSide = (sideOfEdgeEndA == sideOfEdgeEndB) && (sideOfEdgeEndB == sideOfChild);

            if(edgeCrossesRay || edgeTouchesRayOnChildSide || edgeOnChildSide) {
                Log.debug("edgeCrossesRay || edgeTouchesRayOnChildSide || edgeOnChildSide");
                final Cell neighbour = (edge.cells[0] == this) ? edge.cells[1] : edge.cells[0];

                if((neighbour != null) && neighbours.add(neighbour)) {
                    neighbour.getNeighbours(child, neighbours);
                }
            }
        }
    }
}

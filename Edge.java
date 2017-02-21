public class Edge {
    public final double[] ends = new double[4];
    public final Cell[] cells = new Cell[2];

    public Edge(double x1, double y1, double x2, double y2, Cell a, Cell b) {
        ends[0] = x1;
        ends[1] = y1;
        ends[2] = x2;
        ends[3] = y2;
        cells[0] = a;
        cells[1] = b;
    }
}

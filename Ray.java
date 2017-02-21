public class Ray {
    private final Point a;
    private final Point b;

    // Constructs a ray perpendicular to these two points.
    public Ray(Point a, Point b) {
        final double cx = (a.x + b.x) / 2;
        final double cy = (a.y + b.y) / 2;

        final double cax = a.x - cx;
        final double cay = a.y - cy;
        final double cbx = b.x - cx;
        final double cby = b.y - cy;

        final double rax = cay;
        final double ray = -cax;
        final double rbx = cby;
        final double rby = -cbx;

        final double ax = rax + cx;
        final double ay = ray + cy;
        final double bx = rbx + cx;
        final double by = rby + cy;

        this.a = new Point(ax, ay);
        this.b = new Point(bx, by);
    }

    // Constructs a ray parallel to this line.
    public Ray(double[] ends) {
        a = new Point(ends[0], ends[1]);
        b = new Point(ends[2], ends[3]);
    }

    public boolean sameSide(double x1, double y1, double x2, double y2) {
        final double v1 = (b.x - a.x) * (y1 - a.y) - (x1 - a.x) * (b.y - a.y);
        final double v2 = (b.x - a.x) * (y2 - a.y) - (x2 - a.x) * (b.y - a.y);

        return (v1 < 0) == (v2 < 0);
    }

    public Point intersection(Ray other) {
        final Point p1 = a;
        final Point p2 = b;
        final Point p3 = other.a;
        final Point p4 = other.b;

        final double tx = ((p1.x * p2.y - p1.y * p2.x) * (p3.x - p4.x) - (p1.x - p2.x) * (p3.x * p4.y - p3.y * p4.x));
        final double ty = ((p1.x * p2.y - p1.y * p2.x) * (p3.y - p4.y) - (p1.y - p2.y) * (p3.x * p4.y - p3.y * p4.x));

        final double b = ((p1.x - p2.x) * (p3.y - p4.y) - (p1.y - p2.y) * (p3.x - p4.x));

        if(b == 0) {
            return null;
        }

        final double px = tx / b;
        final double py = ty / b;

        return new Point(px, py);
    }
}

public class Point {
    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double get(int n) {
        if(n == 0) {
            return x;
        } else {
            return y;
        }
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.util.*;

public class Main {
    private JFrame mainMap;
    private final boolean fill = true;

    public Main() {
        mainMap = new JFrame();
        mainMap.setResizable(false);

        mainMap.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final List<Point> points = new ArrayList<>();

        // final long seed = System.currentTimeMillis();
        final long seed = 1487683347066L; // Messy
        // final long seed = 1487683341865L; // Crash
        System.out.println("seed: " + seed);

        final Random random = new Random(seed);

        double[] bounds = {10, 10, 289, 289}; // left, top, right, bottom.

        for(int i = 0; i < 4; i++) {
            final double x = bounds[0] + random.nextDouble() * (bounds[2] - bounds[0]);
            final double y = bounds[1] + random.nextDouble() * (bounds[3] - bounds[1]);

            points.add(new Point(x, y));
        }

        final List<Cell> cells = Voronoi.evaluate(points, bounds);

        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                final Color[] colors = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.PINK, Color.ORANGE, Color.MAGENTA};
                int c = 0;

                for(Cell cell : cells) {
                    final Polygon polygon = new Polygon();

                    for(Edge edge : cell.edges) {
                        polygon.addPoint((int)cell.center.x, (int)cell.center.y);
                        polygon.addPoint((int)edge.ends[0], (int)edge.ends[1]);
                        polygon.addPoint((int)edge.ends[2], (int)edge.ends[3]);
                    }

                    g.setColor(colors[c]);
                    if(fill) {
                        g.fillPolygon(polygon);
                    } else {
                        g.drawPolygon(polygon);
                    }

                    c = (c + 1) % colors.length;
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 300);
            }
        };

        mainMap.add(p);
        mainMap.pack();
        mainMap.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Main();
            }
        });
    }
}

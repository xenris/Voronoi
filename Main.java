import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.util.*;

public class Main {
    private JFrame mainMap;
    private final boolean fill = true;
    private final boolean drawPoints = false;
    private final int width = 600;
    private final int height = 600;
    private final double border = 10;
    public static int steps = 0;
    public static int counter = 0;

    public Main() {
        mainMap = new JFrame();
        mainMap.setResizable(false);

        mainMap.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final List<Point> points = new ArrayList<>();

        // final long seed = System.currentTimeMillis();
        // final long seed = 1487917575514L; // Messy
        final long seed = 1487685327795L; // Mostly good.
        Log.log("seed: " + seed);

        final Random random = new Random(seed);

        final double[] bounds = {border, border, width - border - 1, height - border - 1}; // left, top, right, bottom.

        for(int i = 0; i < 1000; i++) {
            final double x = bounds[0] + random.nextDouble() * (bounds[2] - bounds[0]);
            final double y = bounds[1] + random.nextDouble() * (bounds[3] - bounds[1]);

            points.add(new Point(x, y));
        }

        JPanel p = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                final List<Cell> cells = Voronoi.evaluate(points, bounds);

                g.setColor(Color.BLACK);
                g.fillRect(0, 0, width - 1, height - 1);

                final Random colorRandom = new Random(1);

                for(Cell cell : cells) {
                    final Polygon polygon = new Polygon();

                    for(Edge edge : cell.edges) {
                        polygon.addPoint((int)cell.center.x, (int)cell.center.y);
                        polygon.addPoint((int)edge.ends[0], (int)edge.ends[1]);
                        polygon.addPoint((int)edge.ends[2], (int)edge.ends[3]);
                    }

                    g.setColor(new Color(colorRandom.nextInt()));

                    if(fill) {
                        g.fillPolygon(polygon);
                    } else {
                        g.drawPolygon(polygon);
                    }
                }

                if(drawPoints) {
                    for(Cell cell : cells) {
                        g.setColor(Color.WHITE);
                        g.fillOval((int)cell.center.x - 3, (int)cell.center.y - 3, 6, 6);
                    }
                }
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(width, height);
            }
        };

        p.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                    steps = Math.max(steps - 1, 0);
                    counter = 0;
                    Log.log("steps: " + steps);
                    p.revalidate();
                    p.repaint();
                } else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    steps += 1;
                    counter = 0;
                    Log.log("steps: " + steps);
                    p.revalidate();
                    p.repaint();
                }

            }
        });

        p.setFocusable(true);
        p.requestFocusInWindow();

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

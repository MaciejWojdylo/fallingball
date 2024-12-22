import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

//dodanie brancha

public class Main {
    private static long lastTime = System.currentTimeMillis();
    private static int frames = 0;
    private static int fps = 0;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Piłki - Grawitacja i Kolizje");
        frame.setSize(1920, 1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ArrayList<Circle> circles = new ArrayList<>();
        ArrayList<Rectangle> obstacles = new ArrayList<>();
        double gravity = 0.5;
        obstacles.add(new Rectangle(100, 500, 100, 20));
        obstacles.add(new Rectangle(400, 700, 150, 30));
        obstacles.add(new Rectangle(800, 200, 120, 25));
        obstacles.add(new Rectangle(1100, 600, 200, 25));
        obstacles.add(new Rectangle(1600, 800, 180, 30));

        JPanel panel = getjPanel(circles, obstacles);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                circles.add(new Circle(e.getX(), e.getY(), 50, getRandomColor(), getRandomVelocity(), getRandomVelocity(), getRandomMass()));
                panel.repaint();
            }
        });

        Timer timer = new Timer(30, _ -> {
            Iterator<Circle> iterator = circles.iterator();
            while (iterator.hasNext()) {
                Circle c1 = iterator.next();
                if (System.currentTimeMillis() - c1.creationTime > 20000) {
                    iterator.remove();
                    continue;
                }
                c1.x += (int) c1.dx;
                c1.y += (int) c1.dy;
                c1.dy += gravity;
                if (c1.x - c1.radius < 0) {
                    c1.x = c1.radius;
                    c1.dx = -c1.dx;
                }
                if (c1.x + c1.radius > panel.getWidth()) {
                    c1.x = panel.getWidth() - c1.radius;
                    c1.dx = -c1.dx;
                }
                if (c1.y - c1.radius < 0) {
                    c1.y = c1.radius;
                    c1.dy = -c1.dy;
                }
                if (c1.y + c1.radius > panel.getHeight()) {
                    c1.y = panel.getHeight() - c1.radius;
                    c1.dy = -c1.dy;
                }
                for (Circle c2 : circles) {
                    if (c1 != c2 && checkCollision(c1, c2)) {
                        resolveCollision(c1, c2);
                    }
                }
                for (Rectangle obstacle : obstacles) {
                    if (checkCollisionWithRectangle(c1, obstacle)) {
                        resolveCollisionWithRectangle(c1, obstacle);
                    }
                }
            }
            panel.repaint();
            long endTime = System.currentTimeMillis();
            frames++;
            if (endTime - lastTime >= 1000) {
                fps = frames;
                frames = 0;
                lastTime = endTime;
            }
        });
        timer.start();

        frame.add(panel);
        frame.setVisible(true);
    }

    private static JPanel getjPanel(ArrayList<Circle> circles, ArrayList<Rectangle> obstacles) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (Circle circle : circles) {
                    g.setColor(circle.color);
                    g.fillOval(circle.x - circle.radius, circle.y - circle.radius, circle.radius * 2, circle.radius * 2);
                }
                g.setColor(Color.RED);
                for (Rectangle obstacle : obstacles) {
                    g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
                }
                g.setColor(Color.BLACK);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString("FPS: " + fps, getWidth() - 120, 30);
                g.setColor(Color.BLACK);
                g.drawString("Piłki: " + circles.size(), 10, 30);
            }
        };
        panel.setBackground(Color.WHITE);
        return panel;
    }

    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
    }
    private static double getRandomVelocity() {
        Random random = new Random();
        return random.nextDouble() * 4 - 2;
    }

    private static double getRandomMass() {
        Random random = new Random();
        return 1 + random.nextDouble() * 2;
    }
    private static boolean checkCollision(Circle c1, Circle c2) {
        int dx = c1.x - c2.x;
        int dy = c1.y - c2.y;
        int distanceSquared = dx * dx + dy * dy;
        int radiusSum = c1.radius + c2.radius;
        return distanceSquared < radiusSum * radiusSum;
    }
    private static void resolveCollision(Circle c1, Circle c2) {
        double dx = c2.x - c1.x;
        double dy = c2.y - c1.y;
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance == 0) return;
        double nx = dx / distance;
        double ny = dy / distance;
        double overlap = c1.radius + c2.radius - distance;
        c1.x -= (int) (overlap * nx / 2);
        c1.y -= (int) (overlap * ny / 2);
        c2.x += (int) (overlap * nx / 2);
        c2.y += (int) (overlap * ny / 2);
        double dvx = c2.dx - c1.dx;
        double dvy = c2.dy - c1.dy;
        double dotProduct = dvx * nx + dvy * ny;
        if (dotProduct > 0) return;
        double impulse = 2 * dotProduct / (c1.mass + c2.mass);
        c1.dx += impulse * nx * c2.mass;
        c1.dy += impulse * ny * c2.mass;
        c2.dx -= impulse * nx * c1.mass;
        c2.dy -= impulse * ny * c1.mass;
    }
    private static boolean checkCollisionWithRectangle(Circle circle, Rectangle rect) {
        int nearestX = Math.max(rect.x, Math.min(circle.x, rect.x + rect.width));
        int nearestY = Math.max(rect.y, Math.min(circle.y, rect.y + rect.height));

        int dx = circle.x - nearestX;
        int dy = circle.y - nearestY;

        return (dx * dx + dy * dy) < (circle.radius * circle.radius);
    }
    private static void resolveCollisionWithRectangle(Circle circle, Rectangle rect) {
        int centerX = circle.x;
        int centerY = circle.y;

        int nearestX = Math.max(rect.x, Math.min(centerX, rect.x + rect.width));
        int nearestY = Math.max(rect.y, Math.min(centerY, rect.y + rect.height));

        int dx = centerX - nearestX;
        int dy = centerY - nearestY;

        double angleOfReflection = Math.atan2(dy, dx);
        double reflectionSpeed = circle.radius * 0.1;

        circle.dx = reflectionSpeed * Math.cos(angleOfReflection);
        circle.dy = reflectionSpeed * Math.sin(angleOfReflection);
    }
}
class Circle {
    int x, y;
    int radius;
    Color color;
    double dx, dy;
    double mass;
    long creationTime;

    Circle(int x, int y, int radius, Color color, double dx, double dy, double mass) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.dx = dx;
        this.dy = dy;
        this.mass = mass;
        this.creationTime = System.currentTimeMillis();
    }
}

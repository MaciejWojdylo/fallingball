package Frames;

import Shapes.Circle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class GameFrame extends JFrame {
    private static long lastTime = System.currentTimeMillis();
    private static int frames = 0;
    private static int fps = 0;
    private static String typedCode = "";

    GameFrame(Dimension screenSize) {
        setTitle("Game");
        setSize(screenSize);
        ImageIcon icon  = new ImageIcon("src/Images/icon.png");
        ArrayList<Circle> circles = new ArrayList<>();
        ArrayList<Rectangle> obstacles = new ArrayList<>();
        double gravity = 0.5;
        JPanel panel = getjPanel(circles, obstacles);
        JButton infoButton = new JButton();
        infoButton.setText("");
        infoButton.setIcon(icon);
        infoButton.setPreferredSize(new Dimension(icon.getIconWidth(), icon.getIconHeight()));
        infoButton.setBackground(Color.BLACK);
        infoButton.setBorderPainted(false);
        infoButton.addActionListener(_ -> showInfoDialog());
        infoButton.setFocusPainted(false);
        panel.add(infoButton);

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    if (isInRectangle(e.getX(), e.getY())) {
                        Random rand = new Random();
                        int radius = rand.nextInt(30, 50) + 1;
                        circles.add(new Circle(e.getX(), e.getY(), radius, getRandomColor(), getRandomVelocity(), getRandomVelocity(), getRandomMass()));
                    }
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    if (isInRectangle(e.getX(), e.getY())) {
                        boolean rectangleClicked = false;
                        for (Rectangle rect : obstacles) {
                            if (rect.contains(e.getPoint())) {
                                rectangleClicked = true;
                                obstacles.remove(rect);
                                break;
                            }
                        }
                        if (!rectangleClicked) {
                            int width = 100;
                            int height = 20;
                            int x = Math.max(getRectangleX(), Math.min(e.getX() - width / 2, getRectangleX() + getRectangleWidth() - width));
                            int y = Math.max(getRectangleY(), Math.min(e.getY() - height / 2, getRectangleY() + getRectangleHeight() - height));
                            if (e.getX() + width / 2 > getRectangleX() + getRectangleWidth()) {
                                width = getRectangleX() + getRectangleWidth() - e.getX();
                            }
                            if (e.getX() - width / 2 < getRectangleX()) {
                                width = e.getX() - getRectangleX();
                            }
                            if (e.getY() + height / 2 > getRectangleY() + getRectangleHeight()) {
                                height = getRectangleY() + getRectangleHeight() - e.getY();
                            }
                            if (e.getY() - height / 2 < getRectangleY()) {
                                height = e.getY() - getRectangleY();
                            }

                            obstacles.add(new Rectangle(x, y, width, height));
                        }
                    }
                }
                panel.repaint();
            }
        });

        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char key = e.getKeyChar();
                typedCode += key;
                if (typedCode.length() > 5) {
                    typedCode = typedCode.substring(typedCode.length() - 5);
                }
                if (typedCode.equals("kokon")) {
                    Random rand = new Random();
                    for (int i = 0; i < 1000; i++) {
                        int x = rand.nextInt(getRectangleX(), getRectangleX() + getRectangleWidth());
                        int y = rand.nextInt(getRectangleY(), getRectangleY() + getRectangleHeight());
                        circles.add(new Circle(x, y, 50, getRandomColor(), getRandomVelocity(), getRandomVelocity(), getRandomMass()));
                    }
                    panel.repaint();
                    typedCode = "";
                }
            }
        });
        panel.setFocusable(true);

        Timer timer = new Timer(1, _ -> {
            Iterator<Circle> iterator = circles.iterator();
            while (iterator.hasNext()) {
                Circle circle = iterator.next();
                if (System.currentTimeMillis() - circle.creationTime > 20000) {
                    iterator.remove();
                    continue;
                }
                circle.x += (int) circle.dx;
                circle.y += (int) circle.dy;
                circle.dy += gravity;
                if (circle.x - circle.radius < getRectangleX()) {
                    circle.x = getRectangleX() + circle.radius;
                    circle.dx = -circle.dx;
                }
                if (circle.x + circle.radius > getRectangleX() + getRectangleWidth()) {
                    circle.x = getRectangleX() + getRectangleWidth() - circle.radius;
                    circle.dx = -circle.dx;
                }
                if (circle.y - circle.radius < getRectangleY()) {
                    circle.y = getRectangleY() + circle.radius;
                    circle.dy = -circle.dy;
                }
                if (circle.y + circle.radius > getRectangleY() + getRectangleHeight()) {
                    circle.y = getRectangleY() + getRectangleHeight() - circle.radius;
                    circle.dy = -circle.dy;
                }

                for (Circle circle2 : circles) {
                    if (circle != circle2 && checkCollision(circle, circle2)) {
                        resolveCollision(circle, circle2);
                    }
                }
                for (Rectangle obstacle : obstacles) {
                    if (checkCollisionWithRectangle(circle, obstacle)) {
                        resolveCollisionWithRectangle(circle, obstacle);
                    }
                }
            }
            panel.repaint();
            long endTime = System.currentTimeMillis();
            frames++;
            if (endTime - lastTime > 1000) {
                fps = frames;
                frames = 0;
                lastTime = endTime;
            }
        });
        timer.start();
        add(panel);
        setVisible(true);
    }

    private JPanel getjPanel(ArrayList<Circle> circles, ArrayList<Rectangle> obstacles) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(getRectangleX(), getRectangleY(), getRectangleWidth(), getRectangleHeight());

                for (Circle circle : circles) {
                    g.setColor(circle.color);
                    g.fillOval(circle.x - circle.radius, circle.y - circle.radius, circle.radius * 2, circle.radius * 2);
                }

                g.setColor(Color.RED);
                for (Rectangle obstacle : obstacles) {
                    g.fillRect(obstacle.x, obstacle.y, obstacle.width, obstacle.height);
                }

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.PLAIN, 20));
                g.drawString("FPS: " + fps, getWidth() - 120, 30);
                g.setColor(Color.WHITE);
                g.drawString("Piłki: " + circles.size(), 10, 30);
            }
        };
        panel.setBackground(new Color(44, 44, 44));
        return panel;
    }

    private int getRectangleX() {
        return (getWidth() - getRectangleWidth()) / 2;
    }

    private int getRectangleY() {
        return (getHeight() - getRectangleHeight()) / 2;
    }

    private int getRectangleWidth() {
        return getWidth() - 200;
    }

    private int getRectangleHeight() {
        return getHeight() - 200;
    }

    private boolean isInRectangle(int x, int y) {
        return x >= getRectangleX() && x <= getRectangleX() + getRectangleWidth() &&
                y >= getRectangleY() && y <= getRectangleY() + getRectangleHeight();
    }

    private static Color getRandomColor() {
        Random random = new Random();
        int red = random.nextInt(156) + 100;
        int green = random.nextInt(156) + 100;
        int blue = random.nextInt(156) + 100;
        return new Color(red, green, blue);
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

    private static boolean checkCollisionWithRectangle(Circle circle, Rectangle rect) {
        int centerX = circle.x;
        int centerY = circle.y;
        int nearestX = Math.max(rect.x, Math.min(centerX, rect.x + rect.width));
        int nearestY = Math.max(rect.y, Math.min(centerY, rect.y + rect.height));
        int dx = centerX - nearestX;
        int dy = centerY - nearestY;
        return (dx * dx + dy * dy) < (circle.radius * circle.radius);
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

    private static void resolveCollisionWithRectangle(Circle circle, Rectangle rect) {
        int centerX = circle.x;
        int centerY = circle.y;
        int nearestX = Math.max(rect.x, Math.min(centerX, rect.x + rect.width));
        int nearestY = Math.max(rect.y, Math.min(centerY, rect.y + rect.height));
        int dx = centerX - nearestX;
        int dy = centerY - nearestY;
        double angleOfReflection = Math.atan2(dy, dx);
        double reflectionSpeed = Math.sqrt(circle.dx * circle.dx + circle.dy * circle.dy) * 1;
        circle.dx = reflectionSpeed * Math.cos(angleOfReflection);
        circle.dy = reflectionSpeed * Math.sin(angleOfReflection);
    }

    private void showInfoDialog() {
        JDialog infoDialog = new JDialog(this, "Zasady gry", true);
        infoDialog.setSize(400, 300);
        infoDialog.setBackground(Color.BLACK);
        infoDialog.setLocationRelativeTo(this);
        JTextArea textArea = getjTextArea(infoDialog);
        infoDialog.add(textArea, BorderLayout.CENTER);
        JButton okButton = new JButton("OK");
        okButton.addActionListener(_ -> infoDialog.dispose());
        infoDialog.add(okButton, BorderLayout.SOUTH);
        infoDialog.setVisible(true);
    }

    private static JTextArea getjTextArea(JDialog infoDialog) {
        JTextArea textArea = new JTextArea("""
                Balls Simulator - Rules

                Left click - add ball
                Right click - add or remove obstacle
                Balls can only be added in the black rectangle
                The balls disappear after 20 seconds
                Click OK to close""");
        textArea.setEditable(false);
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(new Color(200, 196, 196));
        textArea.setBackground(infoDialog.getBackground());
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        return textArea;
    }
}

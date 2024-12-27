package Shapes;
import java.awt.*;

public class Circle {
    public int x, y;
    public int radius;
    public Color color;
    public double dx, dy;
    public double mass;
    public long creationTime;
    public  String shape;

    public Circle(int x, int y, int radius, Color color, double dx, double dy, double mass,String shape) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.color = color;
        this.dx = dx;
        this.dy = dy;
        this.mass = mass;
        this.creationTime = System.currentTimeMillis();
        this.shape = shape;
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Main{
    public static void main(String[] args) {
        JFrame frame = new JFrame("ZadanieDlaKokona");
        frame.setSize(1920,1080);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ArrayList<Circle> circles = new ArrayList<>();
        JPanel panel = new JPanel(){
          protected void paintComponent(Graphics g){
              super.paintComponent(g);
              g.setColor(Color.BLACK);
              for (Circle circle : circles) {
                  g.fillOval(circle.x - circle.radius, circle.y - circle.radius, circle.radius*2,circle.radius*2);
              }
          }
        };
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        panel.addMouseListener(new MouseAdapter(){
           @Override
            public void mouseClicked(MouseEvent e){
                Point point = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), panel);
                circles.add(new Circle(point.x, point.y, 25,1));
                panel.repaint();
           }
        });
        int delay = 10;
        Timer timer = new Timer(delay,e -> {
            for (Circle circle : circles) {
                float g = (float) (circle.weight / Math.pow(delay,2));
                circle.y += circle.radius+Math.round(g);
            }
            panel.repaint();
        });
        timer.start();
        frame.add(panel);
        frame.setVisible(true);
    }
}
class Circle {
    int x, y;       // Pozycja środka koła
    int radius;     // Promień koła
    float weight;

    Circle(int x, int y, int radius,float weight) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.weight = weight;
    }
}
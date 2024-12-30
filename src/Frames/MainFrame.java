package Frames;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;

public  class MainFrame extends JFrame {
    private static Dimension gameResolutionSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void setGameResolutionSize(Dimension gameResolutionSize) {
        MainFrame.gameResolutionSize = gameResolutionSize;
    }

    private MainFrame() {
        setTitle("Menu");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize);
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                Color color = new Color(1, 1, 1);
                Color color2 = new Color(58, 58, 58);
                GradientPaint gradient = new GradientPaint(0, 0, color, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JButton startButton = new JButton("Start");
        startButton.setSize(400, 100);
        startButton.setFont(new Font("Arial", Font.BOLD, 20));
        startButton.setForeground(Color.WHITE);
        startButton.setLocation((this.getWidth() / 2) - (startButton.getWidth() / 2), (this.getHeight() / 2) - (startButton.getHeight() / 2));
        startButton.setBackground(Color.BLACK);
        startButton.setFocusPainted(false);
        JButton resolutionButton = getjButton(startButton, screenSize);

        startButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                GameFrame gameFrame = new GameFrame(gameResolutionSize);
                setState(JFrame.ICONIFIED);
                gameFrame.setVisible(true);
                gameFrame.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        setState(JFrame.NORMAL);
                    }
                });
            }
        });
        JButton shapesButton = new JButton("Shapes");
        shapesButton.setSize(400, 100);
        shapesButton.setLocation(resolutionButton.getLocation().x, (resolutionButton.getLocation().y + resolutionButton.getHeight() + 10));
        shapesButton.setFont(new Font("Arial", Font.BOLD, 20));
        shapesButton.setForeground(Color.WHITE);
        shapesButton.setBackground(Color.BLACK);
        shapesButton.setFocusPainted(false);

        panel.add(startButton);
        panel.add(resolutionButton);
        panel.add(shapesButton);

        add(panel);
        repaint();
        setContentPane(panel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private static JButton getjButton(JButton startButton, Dimension screenSize) {
        JButton resolutionButton = new JButton("Resolution");
        resolutionButton.setSize(400, 100);
        resolutionButton.setLocation(startButton.getLocation().x, (startButton.getLocation().y + startButton.getHeight() + 10));
        resolutionButton.setFont(new Font("Arial", Font.BOLD, 20));
        resolutionButton.setForeground(Color.WHITE);
        resolutionButton.setBackground(Color.BLACK);
        resolutionButton.setFocusPainted(false);

        resolutionButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ResolutionFrame resolutionFrame = new ResolutionFrame(screenSize);
                resolutionFrame.setVisible(true);
            }
        });
        return resolutionButton;
    }
    public static void startApplication(){
        new MainFrame();
    }
}
class ResolutionFrame extends JFrame {

    public ResolutionFrame(Dimension screenSize) {
        setTitle("Select Resolution");
        setLocation(0, 0);
        setResizable(false);
        String[] resolutionTable = {
                "1366 x 768", "1440 x 900", "1600 x 900",
                "1920 x 1080", "2560 x 1440", "3840 x 2160",
                "5120 x 2880", "7680 x 4320"
        };
        ButtonGroup group = new ButtonGroup();
        Panel resolutionPanel = new Panel();
        resolutionPanel.setLayout(new BoxLayout(resolutionPanel, BoxLayout.Y_AXIS));
        int screenWidth = screenSize.width;
        int visableCheckboxes = 0;
        for (String resolution : resolutionTable) {
            String[] dimensions = resolution.split(" x ");
            int width = Integer.parseInt(dimensions[0]);
            if (width <= screenWidth) {
                JRadioButton checkbox = new JRadioButton(resolution);
                checkbox.setFont(new Font("Arial", Font.PLAIN, 18));
                group.add(checkbox);
                resolutionPanel.add(checkbox);
                visableCheckboxes++;
            }
        }
        JButton acceptButton = getjButton(group);
        resolutionPanel.add(Box.createVerticalStrut(20));
        resolutionPanel.add(acceptButton);
        int elementHeight = 40;
        int margin = 50;
        int windowHeight = visableCheckboxes * elementHeight + margin;
        setSize(400, Math.min(windowHeight, screenSize.height - 50));
        add(resolutionPanel);
    }

    private JButton getjButton(ButtonGroup group) {
        JButton acceptButton = new JButton("Accept");
        acceptButton.setPreferredSize(new Dimension(150, 50));
        acceptButton.setBackground(Color.BLACK);
        acceptButton.setForeground(Color.WHITE);
        acceptButton.setFocusPainted(false);

        acceptButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                ButtonModel selectedModel = group.getSelection();
                if (selectedModel != null) {
                    String resolution = selectedModel.getActionCommand();
                    String[] parts = resolution.split(" x ");
                    Dimension resolutionSize = new Dimension(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                    MainFrame.setGameResolutionSize(resolutionSize);
                }
                dispose();
            }
        });
        for(AbstractButton button : Collections.list(group.getElements())) {
            button.setActionCommand(button.getText());
        }
        return acceptButton;
    }
}
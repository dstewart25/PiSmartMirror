import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Test extends JFrame {
    // Panels for BorderLayout
    private static JPanel leftPanel;
    private static JPanel rightPanel;

    // Panel labels
    private static JLabel dateLabel;
    private static JLabel timeLabel;
    private static JLabel tempLabel;

    public static GraphicsDevice device = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    public void initialize() {
        // Creating panels
        leftPanel = new JPanel();
        leftPanel.setBackground(Color.BLACK);
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);

        // Creating labels
        dateLabel = new JLabel("June 1, 2018");
        dateLabel.setForeground(Color.WHITE);
        timeLabel = new JLabel("12:50 PM");
        timeLabel.setForeground(Color.WHITE);
        tempLabel = new JLabel("90F");
        tempLabel.setForeground(Color.WHITE);

        // Adding elements to panel's
        leftPanel.add(dateLabel);
        leftPanel.add(timeLabel);
        rightPanel.add(tempLabel);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        getContentPane().setBackground(Color.BLACK);
    }

    public Test() {
        super("Smart Mirror");
        setLayout(new BorderLayout(0,0));
        initialize();
        // fuck mike yee
    }

    public static void main(String args[]) {
        Test test = new Test();
        device.setFullScreenWindow(test);
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setExtendedState(JFrame.MAXIMIZED_BOTH);
        test.setVisible(true);
    }
}

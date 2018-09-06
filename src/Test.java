import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Test extends JFrame {
    // Panels for BorderLayout
    private static JPanel leftPanel;
    private static JPanel rightPanel;

    // Panel labels
    private static JLabel dayLabel;
    private static JLabel dateLabel;
    private static JLabel timeLabel;
    private static JLabel tempLabel;

    // Date and time text
    private static String day;
    private static String date;
    private static String time;

    public static GraphicsDevice device = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    public void initialize() {
        // Creating panels
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        leftPanel.setBackground(Color.BLACK);
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);

        getDateAndTime();

        // Creating labels
        dayLabel = new JLabel(day);
        dayLabel.setForeground(Color.WHITE);
        dayLabel.setFont(new Font("Advent Pro", Font.PLAIN, 46));
        dateLabel = new JLabel(date);
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Advent Pro", Font.PLAIN, 30));
        timeLabel = new JLabel(time);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Advent Pro", Font.PLAIN, 38));
        tempLabel = new JLabel("90F");
        tempLabel.setForeground(Color.WHITE);
        tempLabel.setFont(new Font("Advent Pro", Font.PLAIN, 30));

        // Adding elements to panel's
        leftPanel.add(dayLabel);
        leftPanel.add(dateLabel);
        leftPanel.add(timeLabel);
        rightPanel.add(tempLabel);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        getContentPane().setBackground(Color.BLACK);

        // Timer for time
        Timer SimpleTimer = new Timer(1000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                getDateAndTime();
                timeLabel.setText(time);
            }
        });
        SimpleTimer.start();
    }

    public void getDateAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE\nMMM dd, yyyy\nh:mm:ss a");
        LocalDateTime localDateTime = LocalDateTime.now();

        String dateTime = dtf.format(localDateTime);
        //System.out.println(dateTime);
        String split[] = dateTime.split("\n");
        day = split[0];
        date = split[1];
        time = split[2];
    }

    public Test() {
        super("Smart Mirror");
        setLayout(new BorderLayout(0,0));
        initialize();
    }

    public static void main(String args[]) {
        Test test = new Test();
        device.setFullScreenWindow(test);
        test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        test.setExtendedState(JFrame.MAXIMIZED_BOTH);
        test.setVisible(true);
    }
}

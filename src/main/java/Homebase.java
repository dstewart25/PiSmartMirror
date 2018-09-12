import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.param.WeatherData;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

public class Homebase extends JFrame {
    // Panels for BorderLayout
    private static JPanel leftPanel;
    private static JPanel rightPanel;
    private static JPanel weatherPanel;

    // Panel labels
    private static JLabel dayLabel;
    private static JLabel dateLabel;
    private static JLabel timeLabel;
    private static JLabel tempLabel;
    private static JLabel imageLabel;

    // Date and time text
    private static String day;
    private static String date;
    private static String time;

    private static GraphicsDevice device = GraphicsEnvironment
            .getLocalGraphicsEnvironment().getScreenDevices()[0];

    // Holds five day weather forecast
    //public static List<WeatherData> fiveDayForecast = new LinkedList<>();
    private static Weather weather;
    private ImageIcon weatherIcon;

    // declaring string to hold symbol for degree
    private final String DEGREE  = "\u00b0";

    private void initialize() {
        // Creating panels
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        leftPanel.setBackground(Color.BLACK);
        rightPanel = new JPanel();
        rightPanel.setBackground(Color.BLACK);

        // Setting up weather panel
        weatherPanel = new JPanel(new GridLayout(1,2));
        weatherPanel.setBackground(Color.BLACK);

        getDateAndTime();
        try {
            getCurrentWeather();
        } catch (APIException e) {
            e.printStackTrace();
        }

        // Creating labels
        dayLabel = new JLabel(day);
        dayLabel.setForeground(Color.WHITE);
        dayLabel.setFont(new Font("Advent Pro", Font.PLAIN, 52));
        dateLabel = new JLabel(date);
        dateLabel.setForeground(Color.LIGHT_GRAY);
        dateLabel.setFont(new Font("Advent Pro", Font.PLAIN, 30));
        timeLabel = new JLabel(time);
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Advent Pro", Font.PLAIN, 60));
        tempLabel = new JLabel(weather.getCurrentTemp() + DEGREE + "F");
        tempLabel.setForeground(Color.WHITE);
        tempLabel.setFont(new Font("Advent Pro", Font.PLAIN, 60));

        // Image label
        imageLabel = new JLabel(weatherIcon);

        // Adding elements to panel's
        leftPanel.add(dayLabel);
        leftPanel.add(dateLabel);
        leftPanel.add(timeLabel);

        // Adding weather elements
        weatherPanel.add(imageLabel);
        weatherPanel.add(tempLabel);
        rightPanel.add(weatherPanel);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
        getContentPane().setBackground(Color.BLACK);

        // Timer for time
        Timer currentTimeTimer = new Timer(1000, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                getDateAndTime();
                timeLabel.setText(time);
            }
        });
        currentTimeTimer.start();

        // Timer for weather
        Timer weatherTimer = new Timer(1000 * 60, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    getCurrentWeather();
                } catch (APIException e1) {
                    e1.printStackTrace();
                }
                tempLabel.setText(weather.getCurrentTemp() + DEGREE + "F");
            }
        });
        weatherTimer.start();
    }

    private void getDateAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE\nMMM dd, yyyy\nh:mm:ss a");
        LocalDateTime localDateTime = LocalDateTime.now();

        String dateTime = dtf.format(localDateTime);
        //System.out.println(dateTime);
        String split[] = dateTime.split("\n");
        day = split[0];
        date = split[1];
        time = split[2];
    }

    private void getCurrentWeather() throws APIException, NullPointerException {


        // holds data for five day forecast
        List<WeatherData> fiveDayForecast = new LinkedList<>();

        // declaring object of "OWM" class
        OWM owm = new OWM("304c11e7de533a338803ab9aaab95abd");

        // getting current weather data for the "London" city
        CurrentWeather currentWeather = owm.currentWeatherByCityName("Estero");
        weather = new Weather(currentWeather.getCityName(), currentWeather.getMainData().getTempMax(),
                currentWeather.getMainData().getTempMin(), currentWeather.getMainData().getTemp(),
                currentWeather.getWeatherList().get(0).getConditionId());

        // Getting current weather icon
        weatherIcon = weather.getConditionIcon();
    }

    private Homebase() {
        super("Smart Mirror");
        setLayout(new BorderLayout(0,0));
        initialize();
    }

    public static void main(String args[]) {
        Homebase homebase = new Homebase();
        device.setFullScreenWindow(homebase);
        homebase.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        homebase.setExtendedState(JFrame.MAXIMIZED_BOTH);
        homebase.setVisible(true);
        
    }
}

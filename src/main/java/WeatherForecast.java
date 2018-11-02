import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.WeatherData;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

public class WeatherForecast extends JPanel /* JFrame */ {
    private JPanel weatherPanel;
    private JTable weatherTable;
    //private JScrollPane weatherScrollPane;
    private DefaultTableModel weatherTableModel;

    private DecimalFormat df = new DecimalFormat("#");

    private ImageIcon conditionIcon;

    // declaring string to hold symbol for degree
    private final String DEGREE  = "\u00b0";

    // holds data for five day forecast
    private List<WeatherData> fiveDayForecast = new LinkedList<>();

    private void initialize() {
        try {
            getFiveDayForecast();
        } catch (APIException e) {
            e.printStackTrace();
        }

        weatherPanel = new JPanel();
        weatherPanel.setBackground(Color.BLACK);

        populateTable();
        weatherTable = new JTable(weatherTableModel);
        weatherTable.getColumnModel().setColumnMargin(20);
        weatherTable.setPreferredSize(new Dimension(500, 300));
        weatherTable.setTableHeader(null);
        weatherTable.setBorder(null);
        weatherTable.setGridColor(Color.BLACK);
        weatherTable.setBackground(Color.BLACK);
        weatherTable.setForeground(Color.LIGHT_GRAY);
        //weatherTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        weatherTable.setRowHeight(weatherTable.getRowHeight() + 40);
        weatherTable.setFont(new Font("Serif", Font.BOLD, 36));
        /*weatherScrollPane = new JScrollPane(weatherTable,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        weatherScrollPane.setPreferredSize(new Dimension(600, 400));
        weatherScrollPane.setBackground(Color.BLACK);*/

        weatherPanel.add(weatherTable);
    }

    private ImageIcon getConditionIcon(int conditionID) {
        String weatherImagePath;
        //System.out.println("Weather conditionID: " + conditionID);
        if (conditionID >= 200 && conditionID <= 232) { // storms
            switch (conditionID) {
                // thunderstorms image
                case 202:
                    weatherImagePath = "thunderstorms.png";
                case 211:
                    weatherImagePath = "thunderstorms.png";
                case 212:
                    weatherImagePath = "thunderstorms.png";
                case 221:
                    weatherImagePath = "thunderstorms.png";
                case 232:
                    weatherImagePath = "thunderstorms.png";
                    // scattered storms image
                case 200:
                    weatherImagePath = "scattered-storms.png";
                case 201:
                    weatherImagePath = "scattered-storms.png";
                case 210:
                    weatherImagePath = "scattered-storms.png";
                case 230:
                    weatherImagePath = "scattered-storms.png";
                case 231:
                    weatherImagePath = "scattered-storms.png";
                default:
                    weatherImagePath = "sunny.png";
            }
        } else if (conditionID >= 300 && conditionID <= 321) {
            weatherImagePath = "light-rain.png";
        } else if (conditionID >= 500 && conditionID <= 531) {
            weatherImagePath = "rainy.png";
        } else if (conditionID >= 600 && conditionID <= 622) {
            weatherImagePath = "snow.png";
        } else if (conditionID == 800) {
            weatherImagePath = "sunny.png";
        } else if (conditionID == 801 || conditionID == 802) {
            weatherImagePath = "partly-cloudy.png";
        } else if (conditionID == 803 || conditionID == 804) {
            weatherImagePath = "partly-cloudy.png";
        } else {
            weatherImagePath = "sunny.png";
        }

        try {
            //BufferedImage bufferedIcon = ImageIO.read(this.getClass().getResource("WeatherIcons/sunny.png"));
            //testIcon = new ImageIcon(bufferedIcon);

            BufferedImage bufferedImage = ImageIO.read(this.getClass().getResource("WeatherIcons/" + weatherImagePath));
            BufferedImage resizedBufferedImage = resize(bufferedImage, 30, 30);
            conditionIcon = new ImageIcon(resizedBufferedImage);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return conditionIcon;
    }

    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

    private void getFiveDayForecast() throws APIException {
        // declaring object of "OWM" class
        OWM owm = new OWM("304c11e7de533a338803ab9aaab95abd");

        // getting daily weather forecast for Estero
        HourlyWeatherForecast weatherForecast = owm.hourlyWeatherForecastByCityName("Estero");

        // Adding weather data to
        List<WeatherData> forecastList = weatherForecast.getDataList();
        for (int i=0; i<forecastList.size(); i += 8) {
            //System.out.println(forecastList.get(i).toString());
            fiveDayForecast.add(forecastList.get(i));
            /*System.out.println("Day: " + forecastList.get(i).getDateTimeText() +
                    "\nTemperature:"
                    + "\nHigh: " + df.format(forecastList.get(i).getMainData().getTempMax() * (9.0/5.0) - 459.76) + DEGREE + "F"
                    + "\nLow: " + df.format(forecastList.get(i).getMainData().getTempMin() * (9.0/5.0) - 459.76) + DEGREE + "F");*/
        }
    }

    private void populateTable() {
        String[] column = new String[] {"Day", "WeatherIcon", "High", "Low"};
        Object[][] data = new Object[5][4];
        for (int i=0; i<5; i++) {
            Calendar c = Calendar.getInstance();
            c.setTime(fiveDayForecast.get(i).getDateTime());
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
            String day = "";
            switch(dayOfWeek){
                case 1:
                    day="Sun";
                    break;
                case 2:
                    day="Mon";
                    break;
                case 3:
                    day="Tue";
                    break;
                case 4:
                    day="Wed";
                    break;
                case 5:
                    day="Thu";
                    break;
                case 6:
                    day="Fri";
                    break;
                case 7:
                    day="Sat";
                    break;
            }

            data[i][0] = day;
            data[i][1] = getConditionIcon(fiveDayForecast.get(i).getWeatherList().get(0).getConditionId());
            data[i][2] = df.format(fiveDayForecast.get(i).getMainData().getTempMax() * (9.0/5.0) - 459.76) + DEGREE + "F";
            data[i][3] = df.format(fiveDayForecast.get(i).getMainData().getTempMin() * (9.0/5.0) - 459.76) + DEGREE + "F";
        }
        weatherTableModel = new DefaultTableModel(data, column) {
            @Override
            public Class<?> getColumnClass(int column) {
                switch(column) {
                    case 1: return ImageIcon.class;
                    default: return String.class;
                }
            }
        };
    }

    public JPanel getWeatherPanel() {
        return weatherPanel;
    }

    public WeatherForecast() {
        initialize();
    }

    /*public static void main(String args[]) {
        WeatherForecast weatherForecastPanel = new WeatherForecast();
        weatherForecastPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        weatherForecastPanel.setSize(600,400);
        weatherForecastPanel.setVisible(true);
    }*/
}

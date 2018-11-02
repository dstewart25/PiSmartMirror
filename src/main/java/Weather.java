import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.DecimalFormat;

public class Weather {
    private double maxTemp;
    private double minTemp;
    private double currentTemp;
    private String city;
    private int conditionID;
    private ImageIcon conditionIcon;

    private DecimalFormat df = new DecimalFormat("#");
    private String weatherImagePath;

    public Weather(String city, double maxTemp, double minTemp, double currentTemp, int conditionID) {
        this.city = city;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.currentTemp = currentTemp;
        this.conditionID = conditionID;
    }

    public String getMaxTemp() {
        return df.format(maxTemp * (9.0/5.0) - 459.76);
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getMinTemp() {
        return df.format(minTemp * (9.0/5.0) - 459.76);
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public String getCurrentTemp() {
        return df.format(currentTemp * (9.0/5.0) - 459.76);
    }

    public void setCurrentTemp(double currentTemp) {
        this.currentTemp = currentTemp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getConditionID() {
        return conditionID;
    }

    public void setConditionID(int conditionID) {
        this.conditionID = conditionID;
    }

    public ImageIcon getConditionIcon() {
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
            BufferedImage resizedBufferedImage = resize(bufferedImage, 50, 50);
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
}

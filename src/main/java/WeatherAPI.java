import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;

import java.text.DecimalFormat;

public class WeatherAPI {
    public static void main(String[] args)
            throws APIException, NullPointerException {

        // declaring string to hold symbol for degree
        final String DEGREE  = "\u00b0";

        // declaring object of "OWM" class
        OWM owm = new OWM("304c11e7de533a338803ab9aaab95abd");

        // getting current weather data for the "London" city
        CurrentWeather cwd = owm.currentWeatherByCityName("Estero");

        //printing city name from the retrieved data
        System.out.println("City: " + cwd.getCityName());

        // converting kelvin temp to fahrenheit
        double minTempD;
        double maxTempD;
        minTempD = cwd.getMainData().getTempMax() * (9.0/5.0) - 459.76;
        maxTempD = cwd.getMainData().getTempMin() * (9.0/5.0) - 459.76;
        DecimalFormat df = new DecimalFormat("#.#");
        String minTemp = df.format(minTempD);
        String maxTemp = df.format(maxTempD);

        // printing the max./min. temperature
        System.out.println("Temperature: " + minTemp + DEGREE + "F"
                + " / " + maxTemp + DEGREE + "F");
    }
}

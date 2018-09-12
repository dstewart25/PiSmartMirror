import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.DailyWeatherForecast;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.ForecastData;
import net.aksingh.owmjapis.model.param.WeatherData;

import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

public class WeatherAPI {

    public static void main(String[] args)
            throws APIException, NullPointerException {

        // declaring string to hold symbol for degree
        final String DEGREE  = "\u00b0";

        // holds data for five day forecast
        List<WeatherData> fiveDayForecast = new LinkedList<>();

        // declaring object of "OWM" class
        OWM owm = new OWM("304c11e7de533a338803ab9aaab95abd");

        // getting current weather data for the "London" city
        CurrentWeather cwd = owm.currentWeatherByCityName("Estero");

        //printing city name from the retrieved data
        System.out.println("City: " + cwd.getCityName());

        // converting kelvin temp to fahrenheit
        double maxTempD = cwd.getMainData().getTempMax() * (9.0/5.0) - 459.76;
        double minTempD = cwd.getMainData().getTempMin() * (9.0/5.0) - 459.76;
        double currentTempD = cwd.getMainData().getTemp() * (9.0/5.0) - 459.76;
        DecimalFormat df = new DecimalFormat("#.#");
        String minTemp = df.format(minTempD);
        String maxTemp = df.format(maxTempD);
        String currentTemp = df.format(currentTempD);

        // printing the max./min. temperature
        System.out.println("Temperature:"
                + "\nHigh: " + maxTemp + DEGREE + "F"
                + "\nLow: " + minTemp + DEGREE + "F"
                + "\nCurrent: " + currentTemp + DEGREE + "F");

        // getting daily weather forecast for Estero
        HourlyWeatherForecast weatherForecast = owm.hourlyWeatherForecastByCityName("Estero");

        // printing city name from the retrieved data
        System.out.println("\nCity: " + weatherForecast.getCityData().getName());
        System.out.println(cwd.toString());
        System.out.println(cwd.getWeatherList().get(0).getConditionId());

        // Adding weather data to
        List<WeatherData> forecastList = weatherForecast.getDataList();
        /*for (int i=0; i<forecastList.size(); i += 8) {
            //System.out.println(forecastList.get(i).toString());
            fiveDayForecast.add(forecastList.get(i));
            System.out.println("Day: " + forecastList.get(i).getDateTimeText() +
                    "\nTemperature:"
                    + "\nHigh: " + df.format(forecastList.get(i).getMainData().getTempMax() * (9.0/5.0) - 459.76) + DEGREE + "F"
                    + "\nLow: " + df.format(forecastList.get(i).getMainData().getTempMin() * (9.0/5.0) - 459.76) + DEGREE + "F"
                    + "\nCurrent: " + df.format(forecastList.get(i).getMainData().getTemp() * (9.0/5.0) - 459.76) + DEGREE + "F");
        }*/
    }
}

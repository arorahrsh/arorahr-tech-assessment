package nz.co.westpac.swis.service;

import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.model.WeatherDescription;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MockedWeatherInfoService {

    /**
     * Returns a mock Weather data object for the given city with random weather information for today's date.
     * @param city Requested city
     * @return Weather information
     */
    public Weather getWeatherData(String city) {
        return new Weather(
                city,
                generateRandomTemp(),
                "C",
                getCurrentDate(),
                WeatherDescription.random().toString()
        );
    }

    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.now().format(formatter);
    }

    private int generateRandomTemp() {
        return ThreadLocalRandom.current().nextInt(-10, 41);
    }
}

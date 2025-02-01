package nz.co.westpac.swis.service;

import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.model.WeatherDescription;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MockedWeatherInfoService {

    public Weather getWeatherData(City city) {
        return new Weather(
                city.city,
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
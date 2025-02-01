package nz.co.westpac.swis.service;

import nz.co.westpac.swis.model.Weather;
import org.springframework.stereotype.Service;

/**
 * Represents an external API weather service that returns weather information given a city name.
 */
@Service
public interface MockedWeatherInfoService {
    /**
     * Returns a mock Weather data object for the given city with random weather information for today's date.
     * @param city Requested city
     * @return Weather information
     */
    Weather getWeatherData(String city);
}

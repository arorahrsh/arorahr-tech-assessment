package nz.co.westpac.swis.service;

import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WeatherService {
    /**
     * Returns the weather data for the given cities. If exists in cache, then return from cache. Otherwise, call an
     * external mock API service to retrieve the information.
     * @param cities List of cities (must be between 1-3)
     * @return The weather data for input cities.
     */
    List<Weather> getWeatherData(List<City> cities);
}

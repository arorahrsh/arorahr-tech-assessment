package nz.co.westpac.swis.service;

import lombok.AllArgsConstructor;
import nz.co.westpac.swis.config.properties.AppProperties;
import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.repository.WeatherRepository;
import nz.co.westpac.swis.utils.GsonHelper;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class WeatherServiceImpl implements WeatherService {

    private WeatherRepository weatherRepository;

    private AppProperties appProperties;

    private RestClient restClient;

    private Logger logger;

    public List<Weather> getWeatherData(List<City> cities) {
        return cities.stream()
                .map(city -> this.getFromDB(city.getCity()))
                .collect(Collectors.toList());
    }

    private Weather getFromDB(String city) {
        logger.info("Searching if cached weather data exists for city '{}'", city);

        // returns weather information if found in cache, otherwise fetches from external API
        Optional<Weather> weather = this.weatherRepository.findById(city);
        if (weather.isPresent()) {
            logger.info("Found cached weather data for city '{}': {}", city, GsonHelper.toJson(weather.get()));
            return weather.get();
        } else {
            logger.info("Cached weather data not found in database for city '{}'", city);
            return this.weatherRepository.save(getFromExternalApi(city));
        }
    }

    private Weather getFromExternalApi(String city) {
        logger.info("Requesting weather data from external mock service for city '{}'", city);
        return restClient
                .get()
                .uri(appProperties.getMockedWeatherEndpoint() + "/" + city)
                .retrieve()
                .toEntity(Weather.class)
                .getBody();
    }
}

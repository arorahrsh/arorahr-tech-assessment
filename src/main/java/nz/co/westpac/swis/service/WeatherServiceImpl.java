package nz.co.westpac.swis.service;

import nz.co.westpac.swis.config.properties.AppProperties;
import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.repository.WeatherRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private AppProperties appProperties;

    @Autowired
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
            logger.info("Searching if cached weather data exists for city '{}'", city);
            return weather.get();
        } else {
            logger.info("Cached weather data not found in database for city '{}'", city);
            return this.weatherRepository.save(getFromExternalApi(city));
        }
    }

    private Weather getFromExternalApi(String city) {
        logger.info("Requesting weather data from external mock service for city '{}'", city);
        return RestClient.create()
                .get()
                .uri(appProperties.getMockedWeatherEndpoint() + "/" + city)
                .retrieve()
                .toEntity(Weather.class)
                .getBody();
    }
}

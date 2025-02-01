package nz.co.westpac.swis.service;

import nz.co.westpac.swis.config.properties.AppProperties;
import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private AppProperties appProperties;

    public List<Weather> getWeatherData(List<City> cities) {
        return cities.stream()
                .map(city -> getFromExternalApi(city))
                .collect(Collectors.toList());
    }

    private Weather getFromExternalApi(City city) {
        return RestClient.create()
                .get()
                .uri(appProperties.getMockedWeatherEndpoint() + city.city)
                .retrieve()
                .toEntity(Weather.class)
                .getBody();
    }
}

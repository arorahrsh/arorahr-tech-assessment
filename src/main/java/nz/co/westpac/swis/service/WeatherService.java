package nz.co.westpac.swis.service;

import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private MockedWeatherInfoService mockedWeatherInfoService;

    public List<Weather> getWeatherData(List<City> cities) {
        return cities.stream()
                .map(city -> mockedWeatherInfoService.getWeatherData(city))
                .collect(Collectors.toList());
    }
}

package nz.co.westpac.swis.controller;

import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping
    public ResponseEntity<List<Weather>> getWeather(@RequestBody List<City> cities) {
        return ResponseEntity.ok(weatherService.getWeatherData(cities));
    }
}
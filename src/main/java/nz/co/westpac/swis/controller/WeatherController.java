package nz.co.westpac.swis.controller;

import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping
    public ResponseEntity<List<Weather>> getWeather(@RequestBody List<City> cities) {
        return ResponseEntity.ok(
                // return mock response for now
                cities.stream().map(c -> new Weather(c.city, 23, "C", "date", "cloudy")).collect(Collectors.toList())
        );
    }
}
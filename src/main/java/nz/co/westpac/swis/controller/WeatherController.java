package nz.co.westpac.swis.controller;

import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.service.WeatherService;
import nz.co.westpac.swis.utils.GsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.List;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private Logger logger;


    @PostMapping
    public ResponseEntity<List<Weather>> getWeather(@RequestBody List<City> cities) {
        logger.info("Received POST request with JSON payload: {}", GsonHelper.toJson(cities));
        return ResponseEntity.ok(weatherService.getWeatherData(cities));
    }
}
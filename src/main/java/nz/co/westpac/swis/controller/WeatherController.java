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
@RequestMapping("/v1")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private Logger logger;

    @PostMapping("/weather")
    public ResponseEntity<List<Weather>> getWeather(@RequestBody List<City> cities) {
        logger.info("Received POST /v1/weather request with JSON payload: {}", GsonHelper.toJson(cities));
        List<Weather> result = weatherService.getWeatherData(cities);
        logger.info("Returning 200 HTTP status code and JSON payload: {}", GsonHelper.toJson(result));
        return ResponseEntity.ok(result);
    }
}

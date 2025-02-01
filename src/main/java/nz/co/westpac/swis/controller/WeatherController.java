package nz.co.westpac.swis.controller;

import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.service.WeatherService;
import nz.co.westpac.swis.utils.GsonHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private Logger logger;

    @PostMapping("/weather")
    public ResponseEntity<?> getWeather(@RequestBody List<City> cities) {
        logger.info("Received POST /v1/weather request with JSON payload: {}", GsonHelper.toJson(cities));

        List<String> errors = validateRequest(cities);
        if(!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }

        List<Weather> result = weatherService.getWeatherData(cities);
        logger.info("Returning 200 HTTP status code and JSON payload: {}", GsonHelper.toJson(result));
        return ResponseEntity.ok(result);
    }

    private List<String> validateRequest(List<City> cities) {
        List<String> errors = new ArrayList<>();

        if (cities == null || cities.isEmpty()) {
            errors.add("Invalid request body provided.");
        } else if (cities.size() > 3) {
            errors.add("Only a maximum of 3 cities allowed in one request.");
        }

        if (cities != null) {
            errors.addAll(
                    cities.stream()
                            .filter(c -> (c == null || c.getCity().trim().length() < 2))
                            .map(c -> "Requested city name '" + (c == null ? "null" : c.getCity()) + "' is invalid.")
                            .toList());
        }

        return errors;
    }
}

package nz.co.westpac.swis.controller;

import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.service.MockedWeatherInfoService;
import nz.co.westpac.swis.utils.GsonHelper;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mocked-service")
public class MockedWeatherInfoController {

    @Autowired
    private MockedWeatherInfoService mockedWeatherInfoService;

    @Autowired
    private Logger logger;

    @GetMapping("/weather-info")
    public ResponseEntity<Weather> getMockedData(@RequestBody City city) {
        logger.info("Sending GET request to mocked weather API: {}", GsonHelper.toJson(city));
        Weather result = mockedWeatherInfoService.getWeatherData(city);
        logger.info("Received weather data for city '{}' as a JSON response: {}", city.city, GsonHelper.toJson(result));
        return ResponseEntity.ok(result);
    }
}

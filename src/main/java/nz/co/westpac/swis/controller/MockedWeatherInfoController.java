package nz.co.westpac.swis.controller;

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

    @GetMapping("/weather-info/{city}")
    public ResponseEntity<Weather> getMockedData(@PathVariable String city) {
        logger.info("Received GET /mocked-service/weather-info/{}", city);
        Weather result = mockedWeatherInfoService.getWeatherData(city);
        logger.info("Returning weather data from external API: {}", GsonHelper.toJson(result));
        return ResponseEntity.ok(result);
    }
}

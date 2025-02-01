package nz.co.westpac.swis.controller;

import lombok.AllArgsConstructor;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.service.MockedWeatherInfoService;
import nz.co.westpac.swis.utils.GsonHelper;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/mocked-service")
public class MockedWeatherInfoController {

    private MockedWeatherInfoService mockedWeatherInfoService;

    private Logger logger;

    @GetMapping("/weather-info/{city}")
    public ResponseEntity<Weather> getMockedData(@PathVariable String city) {
        logger.info("Received GET /mocked-service/weather-info/{}", city);

        // note: assuming city provided will always be valid
        // hence no extra validation is added here
        Weather result = mockedWeatherInfoService.getWeatherData(city);

        logger.info("Returning weather data from external API: {}", GsonHelper.toJson(result));

        return ResponseEntity.ok(result);
    }
}

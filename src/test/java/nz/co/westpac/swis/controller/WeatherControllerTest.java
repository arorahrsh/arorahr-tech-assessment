package nz.co.westpac.swis.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class WeatherControllerTest {

    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WeatherService weatherService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void whenValidCitiesProvided_thenReturnWeatherData() throws Exception {
        List<City> cities = List.of(new City("Auckland"), new City("Wellington"));
        List<Weather> weatherList = List.of(
                new Weather("Auckland", 25, "C", CURRENT_DATE, "sunny"),
                new Weather("Wellington", 12, "C", CURRENT_DATE, "cloudy"));

        when(weatherService.getWeatherData(cities)).thenReturn(weatherList);

        mockMvc.perform(post("/v1/weather")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cities)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city").value("Auckland"))
                .andExpect(jsonPath("$[0].temp").value(25))
                .andExpect(jsonPath("$[0].unit").value("C"))
                .andExpect(jsonPath("$[0].date").value(CURRENT_DATE))
                .andExpect(jsonPath("$[0].weather").value("sunny"))
                .andExpect(jsonPath("$[1].city").value("Wellington"))
                .andExpect(jsonPath("$[1].temp").value(12))
                .andExpect(jsonPath("$[1].unit").value("C"))
                .andExpect(jsonPath("$[1].date").value(CURRENT_DATE))
                .andExpect(jsonPath("$[1].weather").value("cloudy"));
    }

    @Test
    void whenInvalidCitiesProvided_thenReturnBadRequest() throws Exception {
        List<City> cities = List.of(new City("A"));

        mockMvc.perform(post("/v1/weather")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cities)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0].message").value("Requested city name 'A' is invalid."));
    }
}

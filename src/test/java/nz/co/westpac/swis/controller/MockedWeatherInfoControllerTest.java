package nz.co.westpac.swis.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.service.MockedWeatherInfoService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@AutoConfigureMockMvc
class MockedWeatherInfoControllerTest {

    private static final String MOCK_EXTERNAL_API_BASE_PATH = "/mocked-service/weather-info/";
    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MockedWeatherInfoService mockedWeatherInfoService;

    private Weather mockWeather;

    @BeforeEach
    void setUp() {
        mockWeather = new Weather("Auckland", 25, "C", CURRENT_DATE, "sunny");
    }

    @Test
    void whenGetMockedData_thenReturnWeatherInfo() throws Exception {
        String city = "Auckland";
        when(mockedWeatherInfoService.getWeatherData(city)).thenReturn(mockWeather);

        mockMvc.perform(get(MOCK_EXTERNAL_API_BASE_PATH + city))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Auckland"))
                .andExpect(jsonPath("$.temp").value(25))
                .andExpect(jsonPath("$.unit").value("C"))
                .andExpect(jsonPath("$.date").value(CURRENT_DATE))
                .andExpect(jsonPath("$.weather").value("sunny"));
    }
}

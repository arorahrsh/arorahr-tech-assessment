package nz.co.westpac.swis.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import nz.co.westpac.swis.config.properties.AppProperties;
import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import nz.co.westpac.swis.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class WeatherServiceImplTest {

    private static final String MOCK_EXTERNAL_API_URI = "http://mocked-url.com/info";
    private static final String CURRENT_DATE = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private AppProperties appProperties;

    @Mock
    private RestClient restClient;

    @Mock
    private Logger logger;

    @InjectMocks
    private WeatherServiceImpl underTest;

    @Test
    void whenWeatherDataExistsInDB_thenReturnCachedData() {
        List<City> mockCities = List.of(new City("Auckland"));
        Weather mockWeather = new Weather("Auckland", 25, "C", CURRENT_DATE, "sunny");

        when(weatherRepository.findById("Auckland")).thenReturn(Optional.of(mockWeather));

        List<Weather> result = underTest.getWeatherData(mockCities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Auckland", result.get(0).getCity());
        assertEquals(25, result.get(0).getTemp());
        assertEquals("C", result.get(0).getUnit());
        assertEquals(CURRENT_DATE, result.get(0).getDate());
        assertEquals("sunny", result.get(0).getWeather());

        verify(logger).info("Searching if cached weather data exists for city '{}'", "Auckland");
        verify(logger).info("Found cached weather data for city '{}': {}", "Auckland",
                "{\"city\":\"Auckland\",\"temp\":25,\"unit\":\"C\",\"date\":\"" + CURRENT_DATE + "\",\"weather\":\"sunny\"}");
        verifyNoMoreInteractions(weatherRepository);
    }

    @Test
    void whenWeatherDataNotInDB_thenFetchFromExternalApiAndSave() {
        List<City> mockCities = List.of(new City("Auckland"));
        Weather mockWeather = new Weather("Auckland", 25, "C", CURRENT_DATE, "sunny");

        when(appProperties.getMockedWeatherEndpoint()).thenReturn(MOCK_EXTERNAL_API_URI);
        when(weatherRepository.findById("Auckland")).thenReturn(Optional.empty());
        when(weatherRepository.save(any(Weather.class))).thenReturn(mockWeather);

        // mock RestClient and chained method calls
        RestClient.RequestHeadersUriSpec uriSpecMock = mock(RestClient.RequestHeadersUriSpec.class);
        RestClient.RequestHeadersSpec<?> headersSpecMock = mock(RestClient.RequestHeadersSpec.class);
        RestClient.ResponseSpec responseSpecMock = mock(RestClient.ResponseSpec.class);
        when(restClient.get()).thenReturn(uriSpecMock);
        when(uriSpecMock.uri(MOCK_EXTERNAL_API_URI + "/Auckland")).thenReturn(headersSpecMock);
        when(headersSpecMock.retrieve()).thenReturn(responseSpecMock);
        when(responseSpecMock.toEntity(Weather.class)).thenReturn(ResponseEntity.ok(mockWeather));;

        List<Weather> result = underTest.getWeatherData(mockCities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Auckland", result.get(0).getCity());
        assertEquals(25, result.get(0).getTemp());
        assertEquals("C", result.get(0).getUnit());
        assertEquals(CURRENT_DATE, result.get(0).getDate());
        assertEquals("sunny", result.get(0).getWeather());

        verify(logger).info("Searching if cached weather data exists for city '{}'", "Auckland");
        verify(logger).info("Cached weather data not found in database for city '{}'", "Auckland");
        verify(logger).info("Requesting weather data from external mock service for city '{}'", "Auckland");
        verify(weatherRepository).save(mockWeather);
    }

    @Test
    void whenDuplicateCitiesProvided_OnlyOneWeatherResponseReturned() {
        List<City> mockCities = List.of(new City("Auckland"), new City("Auckland"));
        Weather mockWeather = new Weather("Auckland", 25, "C", CURRENT_DATE, "sunny");
        when(weatherRepository.findById("Auckland")).thenReturn(Optional.of(mockWeather));

        List<Weather> result = underTest.getWeatherData(mockCities);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Auckland", result.get(0).getCity());
        assertEquals(25, result.get(0).getTemp());
        assertEquals("C", result.get(0).getUnit());
        assertEquals(CURRENT_DATE, result.get(0).getDate());
        assertEquals("sunny", result.get(0).getWeather());

        verify(logger).info("Searching if cached weather data exists for city '{}'", "Auckland");
        verify(logger).info("Found cached weather data for city '{}': {}", "Auckland",
                "{\"city\":\"Auckland\",\"temp\":25,\"unit\":\"C\",\"date\":\"" + CURRENT_DATE + "\",\"weather\":\"sunny\"}");
        verifyNoMoreInteractions(weatherRepository);
    }
}

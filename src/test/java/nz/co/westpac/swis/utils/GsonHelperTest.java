package nz.co.westpac.swis.utils;

import nz.co.westpac.swis.model.City;
import nz.co.westpac.swis.model.Weather;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GsonHelperTest {
    @Test
    void whenCityConvertedToJson_thenReturnValidJsonString() {
        City city = new City("Auckland");
        String actualJson = GsonHelper.toJson(city);
        String expectedJson = "{\"city\":\"Auckland\"}";
        assertEquals(expectedJson, actualJson);
    }

    @Test
    void whenWeatherConvertedToJson_thenReturnValidJsonString() {
        Weather weather = new Weather("Auckland", 12, "C", "28/01/2025", "cloudy");
        String actualJson = GsonHelper.toJson(weather);
        String expectedJson = "{\"city\":\"Auckland\",\"temp\":12,\"unit\":\"C\",\"date\":\"28/01/2025\",\"weather\":\"cloudy\"}";
        assertEquals(expectedJson, actualJson);
    }
}

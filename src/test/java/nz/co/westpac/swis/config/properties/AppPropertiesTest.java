package nz.co.westpac.swis.config.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppPropertiesTest {

    private static final String MOCK_EXTERNAL_API_URI = "http://mocked-url.com/info";

    @Autowired
    private AppProperties appProperties;

    @Test
    void testPropertyBinding() {
        assertNotNull(appProperties);
        assertEquals(MOCK_EXTERNAL_API_URI, appProperties.getMockedWeatherEndpoint());
    }
}

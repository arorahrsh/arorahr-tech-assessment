package nz.co.westpac.swis.config.properties;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
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

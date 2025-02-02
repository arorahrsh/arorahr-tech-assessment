package nz.co.westpac.swis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class ApplicationTests {
	@Test
	void whenApplicationStarts_thenContextLoadsSuccessfully() {
		assertDoesNotThrow(() -> Application.main(new String[]{}));
	}
}

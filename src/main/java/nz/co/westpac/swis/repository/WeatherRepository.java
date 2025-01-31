package nz.co.westpac.swis.repository;

import nz.co.westpac.swis.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, String> {
}
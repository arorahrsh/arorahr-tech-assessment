package nz.co.westpac.swis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
public class Weather {
    @Id
    @Getter
    private String city;
    private int temp;
    private String unit;
    private String date;
    private String weather;
}
package nz.co.westpac.swis.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Weather {
    @Id
    private String city;
    private int temp;
    private String unit;
    private String date;
    private String weather;
}
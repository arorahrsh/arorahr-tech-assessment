package nz.co.westpac.swis.model;

import java.util.concurrent.ThreadLocalRandom;

public enum WeatherDescription {
    SUNNY, CLOUDY, FOGGY, RAIN;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

    public static WeatherDescription random() {
        return WeatherDescription.values()[ThreadLocalRandom.current().nextInt(WeatherDescription.values().length)];
    }
}

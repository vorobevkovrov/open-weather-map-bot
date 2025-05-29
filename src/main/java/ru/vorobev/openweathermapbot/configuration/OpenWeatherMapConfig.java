package ru.vorobev.openweathermapbot.configuration;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Data
@Configuration
public class OpenWeatherMapConfig {
    @Value("${open.weather.api.key}")
    private String APIkey;
}

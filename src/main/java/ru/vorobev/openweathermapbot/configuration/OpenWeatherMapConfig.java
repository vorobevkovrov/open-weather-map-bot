package ru.vorobev.openweathermapbot.configuration;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Data
@Configuration
@Service
public class OpenWeatherMapConfig {
    @Value("${open.weather.api.key}")
    private String APIkey;

    public String getAPIkey() {
        return APIkey;
    }
}

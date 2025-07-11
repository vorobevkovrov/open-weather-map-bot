package ru.vorobev.openweathermapbot.configuration;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Getter
@RequiredArgsConstructor
@Data
@Configuration
@Service
public class OpenWeatherMapConfig {
    @Value("${open.weather.api.key}")
    private String APIkey;
    @Value("${open.weather.api.url}")
    private String APIUrl;

    public String getAPIkey() {
        return APIkey;
    }

    public String getAPIUrl() {
        return APIUrl;
    }
}

package ru.vorobev.openweathermapbot.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import ru.vorobev.openweathermapbot.configuration.OpenWeatherMapConfig;
import ru.vorobev.openweathermapbot.entity.weather.TemperatureUnits;
import ru.vorobev.openweathermapbot.service.UrlBuilder;

@Slf4j
@Service
@AllArgsConstructor
public class UrlBuilderImpl implements UrlBuilder {
    private final OpenWeatherMapConfig weatherMapConfig;

    public String buildWeatherUrl(double lat, double lon) {
        log.info(" private String buildWeatherUrl");
        return UriComponentsBuilder.fromUriString(weatherMapConfig.getAPIUrl())
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", weatherMapConfig.getAPIkey())
                .queryParam("units", "metric")
                .queryParam("lang", "ru")
                .build()
                .toUriString();
    }
}

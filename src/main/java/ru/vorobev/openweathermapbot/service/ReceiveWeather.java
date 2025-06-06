package ru.vorobev.openweathermapbot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.web.util.UriComponentsBuilder;
import ru.vorobev.openweathermapbot.entity.Weather.TemperatureUnits;
import ru.vorobev.openweathermapbot.configuration.OpenWeatherMapConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ReceiveWeather {
    private final static Logger LOG = LoggerFactory.getLogger(ReceiveWeather.class);
    private static final String URL_WEATHER = "https://api.openweathermap.org/data/2.5/weather?";
    private final OpenWeatherMapConfig weatherMapConfig;
    private TemperatureUnits temperatureUnits;

    public ReceiveWeather(OpenWeatherMapConfig weatherMapConfig) {
        this.weatherMapConfig = weatherMapConfig;
    }

    private String buildWeatherUrl(double lat, double lon) {
        LOG.info(" private String buildWeatherUrl");
        return UriComponentsBuilder.fromUriString(URL_WEATHER)
                .queryParam("lat", lat)
                .queryParam("lon", lon)
                .queryParam("appid", weatherMapConfig.getAPIkey())
                .queryParam("units", "metric")
                .queryParam("lang","ru")
                .build()
                .toUriString();
    }


    public String getWeatherInDefaultCity(int lat,
                                          int lon) {
        HttpResponse<String> response;
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(buildWeatherUrl(lat, lon)))
                    .GET()
                    .build();
            try {
                response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        LOG.info(response.toString());
        return response.body();
    }
}

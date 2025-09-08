package ru.vorobev.openweathermapbot.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.vorobev.openweathermapbot.entity.weather.WeatherData;
import ru.vorobev.openweathermapbot.service.ConvertDirectionWind;
import ru.vorobev.openweathermapbot.service.ConvertPressureHectopascalToMillimetersOfMercury;
import ru.vorobev.openweathermapbot.service.CurrentWeatherData;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
@AllArgsConstructor
public class CurrentWeatherDataImpl implements CurrentWeatherData {
    private final ConvertPressureHectopascalToMillimetersOfMercury convertToMillimetersOfMercury;
    private final static Logger LOG = LoggerFactory.getLogger(CurrentWeatherDataImpl.class);
    private final UrlBuilderImpl urlBuilder;
    WeatherData weatherData;
    ConvertDirectionWind convertDirectionWind;


    @SneakyThrows
    public String getWeatherInDefaultCity(double lat,
                                          double lon) {
        HttpResponse<String> response;
        ObjectMapper mapper = new ObjectMapper();
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(urlBuilder.buildWeatherUrl(lat, lon)))
                    .GET()
                    .build();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            LOG.info(response.body());
            weatherData = mapper.readValue(response.body(), WeatherData.class);
            LOG.info(response.toString());
            //TODO Окончание города в падеже
            return """
                    Сегодня в %s температура воздуха %.1f°C,
                    ощущается как %.1f°C. Скорость ветра %.1f м/с,
                    Направление ветра %s
                    на небе %d%% облачности.                
                    Атмосферное давление составляет %d мм рт. ст.\
                    """.formatted(
                    weatherData.getName(),
                    weatherData.getMain().getTemp(),
                    weatherData.getMain().getFeelsLike(),
                    weatherData.getWind().getSpeed(),
                    convertDirectionWind.convertDegreesToDirection(weatherData.getWind().getDeg()),
                    weatherData.getClouds().getAll(),
                    convertToMillimetersOfMercury.convertPressure(weatherData.getMain().getPressure()));
        }
    }
}

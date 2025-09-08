package ru.vorobev.openweathermapbot.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.springframework.web.util.UriComponentsBuilder;
import ru.vorobev.openweathermapbot.dto.WeatherDto;
import ru.vorobev.openweathermapbot.entity.weather.TemperatureUnits;
import ru.vorobev.openweathermapbot.configuration.OpenWeatherMapConfig;
import ru.vorobev.openweathermapbot.entity.weather.WeatherData;
import ru.vorobev.openweathermapbot.service.ConvertDirectionWind;
import ru.vorobev.openweathermapbot.service.CurrentWeatherData;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Service
public class CurrentWeatherDataImpl implements CurrentWeatherData {
    private final static Logger LOG = LoggerFactory.getLogger(CurrentWeatherDataImpl.class);
    private static final String URL_WEATHER = "";
    private final OpenWeatherMapConfig weatherMapConfig;
    //private final TemperatureUnits temperatureUnits;
    private final UrlBuilderImpl urlBuilder;
    WeatherDto weatherDto;
    WeatherData weatherData;
    ConvertDirectionWind convertDirectionWind;


    public CurrentWeatherDataImpl(OpenWeatherMapConfig weatherMapConfig, UrlBuilderImpl urlBuilder, ConvertDirectionWind convertDirectionWind) {
        this.weatherMapConfig = weatherMapConfig;
        this.urlBuilder = urlBuilder;
        this.convertDirectionWind = convertDirectionWind;
    }

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

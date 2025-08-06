package ru.vorobev.openweathermapbot.entity.weather;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class WeatherData {
    @JsonProperty
    private Coord coord;
    @JsonProperty
    private List<Weather> weather;
    @JsonProperty
    private String base;
    @JsonProperty
    private Main main;
    @JsonProperty
    private int visibility;
    @JsonProperty
    private Wind wind;
    @JsonProperty
    private Clouds clouds;
    @JsonProperty
    private long dt;
    @JsonProperty
    private Sys sys;
    @JsonProperty
    private int timezone;
    @JsonProperty
    private int id;
    @JsonProperty
    private String name;
    @JsonProperty
    private int cod;

    // Вложенный класс для координат
    @Getter
    @Setter
    public static class Coord {
        @JsonProperty
        private double lon;
        @JsonProperty
        private double lat;
    }

    // Вложенный класс для информации о погоде
    @Getter
    @Setter
    public static class Weather {
        @JsonProperty
        private int id;
        @JsonProperty
        private String main;
        @JsonProperty
        private String description;
        @JsonProperty
        private String icon;
    }

    // Вложенный класс для основной информации
    @Getter
    @Setter
    public static class Main {
        @JsonProperty
        private double temp;
        @JsonProperty("feels_like")
        private double feelsLike;
        @JsonProperty("temp_min")
        private double tempMin;
        @JsonProperty("temp_max")
        private double tempMax;
        @JsonProperty
        private int pressure;
        @JsonProperty
        private int humidity;
        @JsonProperty("sea_level")
        private int seaLevel;
        @JsonProperty("grnd_level")
        private int grndLevel;
    }

    // Вложенный класс для ветра
    @Getter
    @Setter
    public static class Wind {
        @JsonProperty
        private double speed;
        @JsonProperty
        private int deg;
    }

    // Вложенный класс для облаков
    @Getter
    @Setter
    public static class Clouds {
        @JsonProperty
        private int all;
    }

    // Вложенный класс для системной информации
    @Getter
    @Setter
    public static class Sys {
        @JsonProperty
        private String country;
        @JsonProperty
        private long sunrise;
        @JsonProperty
        private long sunset;

    }
}


package ru.vorobev.openweathermapbot.entity.Weather;

import com.fasterxml.jackson.annotation.JsonProperty;

// Вложенные классы:
class Coord {
    private double lon;
    private double lat;
    // Геттеры/сеттеры
}

class Weather {
    private int id;
    private String main;
    private String description;
    private String icon;
    // Геттеры/сеттеры
}

class Main {
    private double temp;
    @JsonProperty("feels_like")
    private double feelsLike;
    @JsonProperty("temp_min")
    private double tempMin;
    @JsonProperty("temp_max")
    private double tempMax;
    private int pressure;
    private int humidity;
    @JsonProperty("sea_level")
    private int seaLevel;
    @JsonProperty("grnd_level")
    private int grndLevel;
    // Геттеры/сеттеры
}

class Wind {
    private double speed;
    private int deg;
    private double gust;
    // Геттеры/сеттеры
}

class Clouds {
    private int all;
    // Геттеры/сеттеры
}

class Sys {
    private String country;
    private long sunrise;
    private long sunset;
    // Геттеры/сеттеры
}
package ru.vorobev.openweathermapbot.service;

public interface CurrentWeatherData {
    String getWeatherInCity(double lat, double lon);
}

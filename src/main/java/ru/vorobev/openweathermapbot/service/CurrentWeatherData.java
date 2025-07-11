package ru.vorobev.openweathermapbot.service;

public interface CurrentWeatherData {
    String getWeatherInDefaultCity(double lat, double lon);
}

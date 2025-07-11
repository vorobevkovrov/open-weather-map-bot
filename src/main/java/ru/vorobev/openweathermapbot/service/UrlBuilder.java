package ru.vorobev.openweathermapbot.service;

public interface UrlBuilder {
    String buildWeatherUrl(double lat, double lon);
}

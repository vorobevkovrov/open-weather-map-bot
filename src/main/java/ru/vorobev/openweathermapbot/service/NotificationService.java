package ru.vorobev.openweathermapbot.service;

public interface NotificationService {
    String beginNotification(String begin);

    String endNotification(String end);

    String every(String every);
}

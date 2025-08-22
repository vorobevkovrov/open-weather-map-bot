package ru.vorobev.openweathermapbot.service;

public interface NotificationService {
    void startNotification(Long chatId, String begin, String end, int every);
  }

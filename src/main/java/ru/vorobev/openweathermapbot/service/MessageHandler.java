package ru.vorobev.openweathermapbot.service;

import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {
    void handleMessage(Update update);
}

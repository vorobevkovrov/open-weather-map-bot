package ru.vorobev.openweathermapbot.service;

public interface MessageSender {
    void sendMessage(long chatId, String text);
}

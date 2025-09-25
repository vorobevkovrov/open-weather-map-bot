package ru.vorobev.openweathermapbot.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SendMessageEvent {
    private final long chatId;
    private final String text;
}

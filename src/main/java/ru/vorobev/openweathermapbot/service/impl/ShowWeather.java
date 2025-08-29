package ru.vorobev.openweathermapbot.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.vorobev.openweathermapbot.util.TelegramBot;

@Slf4j
@Service
//@AllArgsConstructor
public class ShowWeather {
    private final CurrentWeatherDataImpl currentWeatherDataImpl;
    private final TelegramBot telegramBot;

    //TODO необходимо переделать архитектуру и убрать аннотацию @Lazy иначе все падает
    // circular dependencies BeanCurrentlyInCreationException
    public ShowWeather(CurrentWeatherDataImpl currentWeatherDataImpl, @Lazy TelegramBot telegramBot) {
        this.currentWeatherDataImpl = currentWeatherDataImpl;
        this.telegramBot = telegramBot;
    }

    public void showWeatherNow(long chatId) {
        String defaultWeather = currentWeatherDataImpl
                .getWeatherInDefaultCity(59.916668, 30.25);
        telegramBot.sendMessage(chatId, defaultWeather);
        log.info("private void showWeatherNow chatId {}", chatId);
    }
}

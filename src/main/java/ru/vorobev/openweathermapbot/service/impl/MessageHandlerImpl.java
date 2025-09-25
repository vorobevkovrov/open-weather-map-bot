package ru.vorobev.openweathermapbot.service.impl;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.vorobev.openweathermapbot.service.CurrentWeatherData;
import ru.vorobev.openweathermapbot.service.MessageHandler;
import ru.vorobev.openweathermapbot.service.SendMessageEvent;

@Slf4j
@Component
@AllArgsConstructor
public class MessageHandlerImpl implements MessageHandler {
    private final static String WEATHER_NOW = "/showWeatherNow";
    private final static String START = "/start";
    private final CurrentWeatherData currentWeatherData;
    //  private final MessageSender messageSender;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void handleMessage(Update update) {
        log.debug("onUpdateReceived(Update update){}", update);
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        log.info("message {}", message);
        switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                start(chatId, userName);
            }
            case WEATHER_NOW -> {
                showWeatherNow(chatId);
                log.info("case WEATHER_NOW -> showWeatherNow(chatId) {}", chatId);
            }
            case "Погода сейчас" -> showWeatherNow(chatId);
            default -> unknownCommand(chatId);
        }
    }

    private void start(long chatId, String userName) {
        var text = """
                 Добро пожаловать в бот, %s!               \s
                 Здесь Вы сможете узнать погоду                \s
                 Для этого воспользуйтесь командами:
                 /showWeatherNow показывает погоду на текущий момент \s
                 Дополнительные команды:
                 /help - получение справки
                \s""";
        var formattedText = String.format(text, userName);
        sendMessage(chatId, formattedText);
    }

    @SneakyThrows
    private void sendMessage(long chatId, String text) {
        eventPublisher.publishEvent(new SendMessageEvent(chatId, text));
        //  messageSender.sendMessage(chatId, text);
        log.info("public void sendMessage(long chatId, String text) {} {}", chatId, text);
    }


    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }

    private void showWeatherNow(long chatId) {
        String defaultWeather = currentWeatherData
                .getWeatherInCity(59.916668, 30.25);
        sendMessage(chatId, defaultWeather);
        log.info("private void showWeatherNow chatId {}", chatId);
    }
}

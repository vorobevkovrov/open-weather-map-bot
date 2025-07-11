package ru.vorobev.openweathermapbot.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.vorobev.openweathermapbot.configuration.BotConfig;
import ru.vorobev.openweathermapbot.configuration.OpenWeatherMapConfig;
import ru.vorobev.openweathermapbot.service.impl.CurrentWeatherDataImpl;

@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final static String WEATHER_NOW = "/showWeatherNow";
    private final static String START = "/start";
    private final static Logger LOG = LoggerFactory.getLogger(TelegramBot.class);
    private final OpenWeatherMapConfig openWeatherMapConfig;
    private final BotConfig botConfig;
    private final CurrentWeatherDataImpl currentWeatherDataImpl;

    public TelegramBot(OpenWeatherMapConfig openWeatherMapConfig, BotConfig botConfig, CurrentWeatherDataImpl currentWeatherDataImpl) {
        this.openWeatherMapConfig = openWeatherMapConfig;
        this.botConfig = botConfig;
        this.currentWeatherDataImpl = currentWeatherDataImpl;
    }

    @Override
    public void onUpdateReceived(Update update) {
        LOG.info("onUpdateReceived(Update update){}", update);
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        String message = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        LOG.info("message {}", message);
        switch (message) {
            case START -> {
                String userName = update.getMessage().getChat().getUserName();
                start(chatId, userName);
            }
            case WEATHER_NOW -> showWeatherNow(chatId);
            default -> unknownCommand(chatId);
        }
    }


    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }


    private void showWeatherNow(long chatId) {
        String defaultWeather = currentWeatherDataImpl
                .getWeatherInDefaultCity(59.916668, 30.25);
        sendMessage(chatId, defaultWeather);
        LOG.info("private void showWeatherNow(long chatId)");
    }


    public void sendMessage(long chatId, String text) {
        var sendMessage = new SendMessage(String.valueOf(chatId), text);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            LOG.error("Ошибка отправки сообщения ", e);
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

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    public String getBotToken() {
        return botConfig.getBotToken();
    }
}


package ru.vorobev.openweathermapbot.util;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.vorobev.openweathermapbot.configuration.BotConfig;
import ru.vorobev.openweathermapbot.configuration.OpenWeatherMapConfig;
import ru.vorobev.openweathermapbot.service.impl.CurrentWeatherDataImpl;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final static String WEATHER_NOW = "/showWeatherNow";
    private final static String START = "/start";
    private final OpenWeatherMapConfig openWeatherMapConfig;
    private final BotConfig botConfig;
    private final CurrentWeatherDataImpl currentWeatherDataImpl;
    private final ReplyKeyboardMarkup keyboardMarkup;
    private final List<KeyboardRow> keyboardRows;


    @Override
    public void onUpdateReceived(Update update) {
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

    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }

    private void showWeatherNow(long chatId) {
        String defaultWeather = currentWeatherDataImpl
                .getWeatherInDefaultCity(59.916668, 30.25);
        sendMessage(chatId, defaultWeather);
        log.info("private void showWeatherNow chatId {}", chatId);
    }

    @SneakyThrows
    public void sendMessage(long chatId, String text) {
        var sendMessage = new SendMessage(String.valueOf(chatId), text);
        keyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(keyboardMarkup);
        execute(sendMessage);
        log.info("public void sendMessage(long chatId, String text) {} {}", chatId, text);
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

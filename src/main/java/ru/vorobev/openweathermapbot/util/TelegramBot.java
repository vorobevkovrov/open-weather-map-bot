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
import ru.vorobev.openweathermapbot.service.NotificationService;
import ru.vorobev.openweathermapbot.service.impl.CurrentWeatherDataImpl;


import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@AllArgsConstructor
@Component
//TODO Рефактор этого класса
public class TelegramBot extends TelegramLongPollingBot {
    private final static String WEATHER_NOW = "/showWeatherNow";
    private final static String START = "/start";
    private final OpenWeatherMapConfig openWeatherMapConfig;
    private final BotConfig botConfig;
    private final CurrentWeatherDataImpl currentWeatherDataImpl;
    private final ReplyKeyboardMarkup keyboardMarkup;
    private final List<KeyboardRow> keyboardRows;
    private final NotificationService notificationService;
    private final Map<Long, UserSession> userSessions = new ConcurrentHashMap<>();

    private enum State {
        WAITING_START_TIME,
        WAITING_END_TIME,
        WAITING_PERIOD,
        NONE
    }

    private static class UserSession {
        State state = State.NONE;
        String startTime;
        String endTime;
        String period;
    }

    @Override
    public void onUpdateReceived(Update update) {
        log.debug("onUpdateReceived(Update update){}", update);
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        String messageText = update.getMessage().getText();
        Long chatId = update.getMessage().getChatId();
        Long userId = update.getMessage().getFrom().getId();
        UserSession session = userSessions.computeIfAbsent(userId, k -> new UserSession());
        log.info("Received message: {}", messageText);
        // Обрабатываем команды без состояния
        if (messageText.equals(START)) {
            String userName = update.getMessage().getChat().getUserName();
            start(chatId, userName);
            return;
        } else if (messageText.equals(WEATHER_NOW) || messageText.equals("Погода сейчас")) {
            showWeatherNow(chatId);
            return;
        }

        // Обрабатываем состояния
        switch (session.state) {
            case WAITING_START_TIME:
                log.info("Processing WAITING_START_TIME");
                if (isValidTimeFormat(messageText)) {
                    session.startTime = messageText;
                    sendMessage(chatId, "Введите время окончания (например, 22.00):");
                    session.state = State.WAITING_END_TIME;
                } else {
                    sendMessage(chatId, "Неверный формат! Введите время как hh.mm (например 08.30)");
                }
                break;

            case WAITING_END_TIME:
                log.info("Processing WAITING_END_TIME");
                if (isValidTimeFormat(messageText)) {
                    session.endTime = messageText;
                    sendMessage(chatId, "Введите периодичность рассылки в минутах (например, 30):");
                    session.state = State.WAITING_PERIOD;
                } else {
                    sendMessage(chatId, "Неверный формат! Введите время как hh.mm");
                }
                break;

            case WAITING_PERIOD:
                log.info("Processing WAITING_PERIOD");
                if (messageText.matches("\\d+")) {
                    session.period = messageText;
                    // Сохраняем настройки и запускаем расписание
                    saveAndScheduleNotifications(chatId, session);
                    sendMessage(chatId, String.format(
                            "Настройки сохранены:\nНачало: %s\nКонец: %s\nПериод: %s минут",
                            session.startTime, session.endTime, session.period));
                    session.state = State.NONE;
                    notificationService.startNotification(chatId, session.startTime, session.endTime, Integer.parseInt(session.period));
                } else {
                    sendMessage(chatId, "Неверный формат! Введите число минут");
                }
                break;

            case NONE:
            default:
                // Обрабатываем команды, когда нет активного состояния
                if (messageText.equals("Установить расписание")) {
                    log.info("Starting schedule setup");
                    sendMessage(chatId, "Введите время начала рассылки (например, 08.00):");
                    session.state = State.WAITING_START_TIME;
                } else {
                    unknownCommand(chatId);
                }
                break;
        }
    }

    private void saveAndScheduleNotifications(Long chatId, UserSession session) {
        log.info("private void saveAndScheduleNotifications(Long chatId, UserSession session)");

    }


    private void unknownCommand(Long chatId) {
        var text = "Не удалось распознать команду!";
        sendMessage(chatId, text);
    }


    public void showWeatherNow(long chatId) {
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
        //   log.info("public void sendMessage(long chatId, String text) {} {}", chatId, text);
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

    private boolean isValidTimeFormat(String time) {
        return time.matches("^([0-1]?[0-9]|2[0-3])\\.[0-5][0-9]$");
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

package ru.vorobev.openweathermapbot.util;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.vorobev.openweathermapbot.configuration.BotConfig;
import ru.vorobev.openweathermapbot.service.MessageHandler;
import ru.vorobev.openweathermapbot.service.MessageSender;
import ru.vorobev.openweathermapbot.service.SendMessageEvent;

import java.util.List;


@Slf4j
@AllArgsConstructor
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final MessageHandler messageHandler;
    private final BotConfig botConfig;
    private final ReplyKeyboardMarkup keyboardMarkup;
    private final List<KeyboardRow> keyboardRows;

    @Override
    public void onUpdateReceived(Update update) {
        messageHandler.handleMessage(update);
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

//    @EventListener
//    @SneakyThrows
//    //  public void sendMessage(long chatId, String text) {
//    public void sendMessage(SendMessage event) {
//        var sendMessage = new SendMessage(String.valueOf(event.getChatId()), event.getText());
//        keyboardMarkup.setKeyboard(keyboardRows);
//        sendMessage.setReplyMarkup(keyboardMarkup);
//        execute(sendMessage);
//        log.info("public void sendMessage(long chatId, String text) {} {}", event.getChatId(), event.getText());
//    }
    @EventListener
    @SneakyThrows
    public void sendMessage(SendMessageEvent event) {
        var sendMessage = new SendMessage(String.valueOf(event.getChatId()), event.getText());
        keyboardMarkup.setKeyboard(keyboardRows);
        sendMessage.setReplyMarkup(keyboardMarkup);
        execute(sendMessage);
    }
}

package ru.vorobev.openweathermapbot.configuration.util;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.vorobev.openweathermapbot.configuration.BotConfig;
import ru.vorobev.openweathermapbot.service.Weather;

import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final SendMessage sendMessage = new SendMessage();
    private final Update update = new Update();
    private final BotConfig botConfig;
    private final Weather weather;

    public TelegramBot(BotConfig botConfig, Weather weather) {
        this.botConfig = botConfig;
        this.weather = weather;
    }

    @Override
    public void onUpdateReceived(Update update) {
//        String messageText = update.getMessage().getText();
//        long chatId = update.getMessage().getChatId();
//        String memberName = update.getMessage().getFrom().getFirstName();
//        System.out.println("public void onUpdatesReceived(List<Update> updates) " + update);
//        System.out.println("Message text - " + messageText);
//        switch (messageText) {
//            case ("/start"):
//                System.out.println("case /start");
//                startCommand(chatId, update.getMessage().getText());
//                sendMessage.setChatId(chatId);
//                sendMessage.setText("Hello, I'm a Telegram bot.");
//                break;
//            case ("/stop"):
//                System.out.println("Not implemented yet");
//                break;
//            case ("/showWeatherNow"):
//                System.out.println("showWeatherNow");
//                break;
//            default:
//                System.out.println("Not found!!!!");

//        }
    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        System.out.println("public void onUpdatesReceived(List<Update> updates) " + updates);
        if (!updates.isEmpty() && updates.getFirst().getMessage().hasText()) {
            String messageText = updates.getFirst().getMessage().getText();
            System.out.println("Message text - " + messageText);
            long chatId = updates.getFirst().getMessage().getChatId();
            switch (messageText) {
                case ("/start"):
                    sendMessage.setText("Hello, I'm a Telegram bot.");
                    System.out.println("case /start");
                    startCommand(chatId, updates.getFirst().getMessage().getForwardSenderName());
                    break;
                case ("/stop"):
                    System.out.println("Not implemented yet");
                    break;
                case ("/showWeatherNow"):
                    System.out.println("showWeatherNow");
                    break;
                default:
                    System.out.println("Not found!!!!");
            }
        }
        super.onUpdatesReceived(updates);
    }

    @SneakyThrows
    private void startCommand(long chatId, String name) {
        String answer = "Hi " + name + " " + chatId
                + " some day i will show weather";
      //  sendMessage.setText(answer);
        sendMessage.setChatId(chatId);
        sendMessage.setText(weather.getWeatherInDefaultCity(59,30,"1656779f5e2ff402f9c1ae1c3c998d10"));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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

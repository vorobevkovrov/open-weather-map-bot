package ru.vorobev.openweathermapbot.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import ru.vorobev.openweathermapbot.util.TelegramBot;

import java.time.LocalDateTime;
import java.util.TimeZone;

@Slf4j
@EnableScheduling
@Service
@AllArgsConstructor
public class DynamicSchedulerService {
    private final TaskScheduler taskScheduler;
    private final ShowWeather showWeather;
    //   private final TelegramBot telegramBot;

    public void scheduleTask(Long chatId, String startTime, String endTime, int intervalMinutes) {
        log.info("private String scheduleTask chatId {}, startTime {}, endTime {}, intervalMinutes {}",
                chatId, startTime, endTime, intervalMinutes);
        String cronExpression = createCronExpression(startTime, endTime, intervalMinutes);
        taskScheduler.schedule(this.executeTask(chatId),
                new CronTrigger(cronExpression, TimeZone.getDefault()));
    }

    private String createCronExpression(String startTime, String endTime, int intervalMinutes) {
        log.info("Creating cron expression for startTime: {}, endTime: {}, interval: {} minutes",
                startTime, endTime, intervalMinutes);

        // Разделяем время с экранированием точки
        String[] startParts = startTime.split("\\.");
        String[] endParts = endTime.split("\\.");

        int startHour = Integer.parseInt(startParts[0]);
        int startMinute = Integer.parseInt(startParts[1]); // Добавляем минуты
        int endHour = Integer.parseInt(endParts[0]);

        // Создаем корректное cron-выражение для Spring (@Scheduled)
        return String.format("0 %d/%d %d-%d * * *", startMinute, intervalMinutes, startHour, endHour);
    }

    public Runnable executeTask(Long chatId) {
        log.info("DynamicSchedulerService private Runnable executeTask");
        return () -> showWeather.showWeatherNow(chatId);
    }
}
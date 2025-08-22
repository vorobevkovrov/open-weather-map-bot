package ru.vorobev.openweathermapbot.service.impl;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.TimeZone;

@EnableScheduling
@Service
public class DynamicSchedulerService {
    private final TaskScheduler taskScheduler;

    public DynamicSchedulerService(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void scheduleTask(String startTime, String endTime, int intervalMinutes) {
        String cronExpression = createCronExpression(startTime, endTime, intervalMinutes);

        taskScheduler.schedule(
                this::executeTask,
                new CronTrigger(cronExpression, TimeZone.getDefault())
        );
    }

    private String createCronExpression(String startTime, String endTime, int intervalMinutes) {
        // Парсим время (формат "HH:mm")
        String[] startParts = startTime.split(":");
        String[] endParts = endTime.split(":");

        int startHour = Integer.parseInt(startParts[0]);
        int endHour = Integer.parseInt(endParts[0]);

        return String.format("0 */%d %d-%d * * *", intervalMinutes, startHour, endHour);
    }

    private void executeTask() {
        System.out.println("Динамическая задача выполняется: " + new Date());
    }
}
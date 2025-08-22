package ru.vorobev.openweathermapbot.service.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.vorobev.openweathermapbot.service.NotificationService;

@Service
@Slf4j
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private DynamicSchedulerService schedulerService;

    @Override
    public void startNotification(Long chatId, String begin, String end, int every) {
        schedulerService.scheduleTask(begin, end, every);
    }
}

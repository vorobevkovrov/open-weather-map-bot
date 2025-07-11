package ru.vorobev.openweathermapbot.service.impl;

import org.springframework.stereotype.Service;
import ru.vorobev.openweathermapbot.service.ConvertDirectionWind;

import java.util.List;

@Service
public class ConvertDirectionWindImpl implements ConvertDirectionWind {
    public String convertDegreesToDirection(int degrees) {
        // Нормализация градусов в диапазон [0, 360)
        degrees = ((degrees % 360) + 360) % 360;

        // Определяем направление по секторам
        if (degrees < 23) return "северный";          // [0, 22]
        if (degrees < 68) return "северо-восточный";  // [23, 67]
        if (degrees < 113) return "восточный";        // [68, 112]
        if (degrees < 158) return "юго-восточный";    // [113, 157]
        if (degrees < 203) return "южный";            // [158, 202]
        if (degrees < 248) return "юго-западный";     // [203, 247]
        if (degrees < 293) return "западный";         // [248, 292]
        if (degrees < 338) return "северо-западный";  // [293, 337]
        return "северный";                            // [338, 359]
    }
}


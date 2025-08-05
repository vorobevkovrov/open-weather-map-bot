package ru.vorobev.openweathermapbot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.vorobev.openweathermapbot.service.ConvertPressureHectopascalToMillimetersOfMercury;

@Service
@RequiredArgsConstructor
public class ConvertPressureHectopascalToMillimetersOfMercuryImpl implements
        ConvertPressureHectopascalToMillimetersOfMercury {

    /**
     * Конвертирует давление из гектопаскалей (гПа) в миллиметры ртутного столба(мм.рт.ст.)
     * по формуле мм рт. ст. = гПа * 0,75006
     *
     * @param pressure давление в гектопаскалях
     * @return давление в миллиметрах ртутного столба
     */
    @Override
    public int convertPressure(int pressure) {
        return (int) (pressure * 0.75006);
    }
}

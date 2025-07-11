package ru.vorobev.openweathermapbot.entity.weather;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**Температура доступна в градусах Фаренгейта, Цельсия и Кельвина.

 Для температуры в градусах Фаренгейта используйте units=imperial
 Для температуры в градусах Цельсия используйте units=metric
 По умолчанию используется температура в градусах Кельвина, нет необходимости использовать параметр units в вызове API
 *
 */
@Getter
@Component
@RequiredArgsConstructor
//TODO Реализовать со временем сейчас заглушка  private String buildWeatherUrl(double lat, double lon,String units)
public class TemperatureUnits {
    private String units;
}

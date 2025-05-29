package ru.vorobev.openweathermapbot.Model.Weather;

import lombok.Data;

@Data
public class Wind {
    /**
     *    Wind speed. Unit Default: meter/sec,
     *    Metric: meter/sec,
     *    Imperial: miles/hour
     */
    private float windSpeed;
    /**
     * Wind direction, degrees (meteorological)
     */
    private int windDeg;
    /**
     * Wind gust. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour
     */
    private float windGust;
}

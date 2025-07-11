package ru.vorobev.openweathermapbot.entity.weather;

import lombok.Data;

@Data
public class Coordination {
    /**
     *  Longitude of the location
     */
    private int lon ;
    /**
     *  Latitude of the location
     */
    private int lat ;
}

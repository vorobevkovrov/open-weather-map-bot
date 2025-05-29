package ru.vorobev.openweathermapbot.Model.Weather;

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

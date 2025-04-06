package ru.vorobev.openweathermapbot;

import org.springframework.boot.SpringApplication;

public class TestOpenWeatherMapBotApplication {

    public static void main(String[] args) {
        SpringApplication.from(OpenWeatherMapBotApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}

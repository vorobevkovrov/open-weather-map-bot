package ru.vorobev.openweathermapbot.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.vorobev.openweathermapbot.Model.Weather.Coordination;
import ru.vorobev.openweathermapbot.configuration.OpenWeatherMapConfig;

@RestController
@RequiredArgsConstructor
@RequestMapping("https://api.openweathermap.org/data/2.5/weather?")
public class Weather {
    OpenWeatherMapConfig openWeatherMapConfig;
    Coordination coordination;

    //TODO сбор урла из данных
    // String url = coordination.getLat() + coordination.getLon() + openWeatherMapConfig.getAPIkey();

    //    @GetMapping("lat=59&lon=30&appid=1656779f5e2ff402f9c1ae1c3c998d10")
    @GetMapping("/")
    public String getWeatherInDefaultCity(@RequestParam int lat,
                                          @RequestParam int lon,
                                          @RequestParam String appid) {
        RestTemplate restTemplate = new RestTemplate();
        String string = String.valueOf(restTemplate.getForEntity("https://api.openweathermap.org/data/2.5/weather?{lat}"  + lon + appid, String.class));
        System.out.println(string);
        return "getWeatherInDefaultCity";
    }
}

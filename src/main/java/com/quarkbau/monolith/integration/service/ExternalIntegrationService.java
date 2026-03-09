package com.quarkbau.monolith.integration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ExternalIntegrationService {

    // This would normally use RestTemplate or WebClient with API keys from
    // properties
    // For now, we simulate the output to show the logic integration

    public Map<String, Object> getTrafficData(Double lat, Double lng) {
        log.info("Fetching traffic data for coordinates: {}, {}", lat, lng);
        Map<String, Object> data = new HashMap<>();
        // Logic: Simulate higher traffic in city centers or during rush hour
        data.put("trafficDensity", 0.6); // 0.0 to 1.0
        data.put("congestionLevel", "MEDIUM");
        data.put("averageSpeed", 35); // km/h
        return data;
    }

    public Map<String, Object> getWeatherData(Double lat, Double lng) {
        log.info("Fetching weather data for coordinates: {}, {}", lat, lng);
        Map<String, Object> data = new HashMap<>();
        data.put("temperature", 12.5);
        data.put("condition", "RAIN");
        data.put("windSpeed", 15.0);
        data.put("precipitationProbability", 0.8);
        return data;
    }
}

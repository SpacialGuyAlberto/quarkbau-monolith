package com.quarkbau.monolith.safety.service;

import com.quarkbau.monolith.integration.service.ExternalIntegrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SafetyPlanningService {

    private final ExternalIntegrationService externalApiService;

    public Map<String, Object> generateSafetyPlan(Double lat, Double lng, Double length) {
        Map<String, Object> traffic = externalApiService.getTrafficData(lat, lng);
        Map<String, Object> weather = externalApiService.getWeatherData(lat, lng);

        Map<String, Object> plan = new HashMap<>();
        plan.put("coordinates", Map.of("lat", lat, "lng", lng));
        plan.put("segmentLength", length);

        // Automated Fencing Calculation
        double congestion = (double) traffic.get("trafficDensity");
        String weatherCond = (String) weather.get("condition");

        List<String> requiredMaterials = new ArrayList<>();
        int barrierCount = (int) Math.ceil(length / 2.0); // One barrier every 2.0m for higher safety
        requiredMaterials.add(barrierCount + " x Standard Baken (VZ 600) with Bases");

        // Advanced Logic for Traffic Control
        if (congestion > 0.8) {
            requiredMaterials.add("1 x Mobile Traffic Light System (LSA - Ampel)");
            plan.put("suggestedRegelplan", "B I/2 + LSA (High Traffic Control)");
            plan.put("requiresPermit", true);
        } else if (congestion > 0.5 || "RAIN".equals(weatherCond)) {
            requiredMaterials.add((barrierCount / 2) + " x Warning Lights (Type D)");
            plan.put("suggestedRegelplan", "B I/2 (Extended Security)");
            plan.put("requiresPermit", false);
        } else {
            plan.put("suggestedRegelplan", "B I/1 (Standard)");
            plan.put("requiresPermit", false);
        }

        if (length > 40) {
            requiredMaterials.add("2 x Pre-warning Signs (VZ 123 + 200m)");
        }

        plan.put("requiredMaterials", requiredMaterials);
        plan.put("environmentalContext", Map.of("traffic", traffic, "weather", weather));
        plan.put("safetyScore", calculateSafetyScore(traffic, weather));

        return plan;
    }

    private int calculateSafetyScore(Map<String, Object> traffic, Map<String, Object> weather) {
        int score = 100;
        if ("RAIN".equals(weather.get("condition")))
            score -= 15;
        if ((double) traffic.get("trafficDensity") > 0.7)
            score -= 20;
        return Math.max(0, score);
    }
}

package com.quarkbau.monolith.graph.controller;

import com.quarkbau.monolith.graph.service.NetworkIntelligenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/intelligence")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Adjust in production
public class NetworkIntelligenceController {

    private final NetworkIntelligenceService intelligenceService;

    @GetMapping("/risk/segment/{segmentId}")
    public ResponseEntity<List<Map<String, Object>>> calculateRiskForSegment(@PathVariable Long segmentId) {
        return ResponseEntity.ok(intelligenceService.calculateRiskForSegment(segmentId));
    }

    @GetMapping("/route/optimal")
    public ResponseEntity<List<Map<String, Object>>> calculateOptimalRoute(
            @RequestParam Long heupId, 
            @RequestParam Long popId) {
        return ResponseEntity.ok(intelligenceService.calculateOptimalRoute(heupId, popId));
    }

    @GetMapping("/capacity/alerts")
    public ResponseEntity<List<Map<String, Object>>> getHighCapacityAlerts() {
        return ResponseEntity.ok(intelligenceService.getHighCapacityAlerts());
    }
}

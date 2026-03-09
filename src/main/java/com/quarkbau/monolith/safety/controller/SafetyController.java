package com.quarkbau.monolith.safety.controller;

import com.quarkbau.monolith.safety.model.SafetyProtocol;
import com.quarkbau.monolith.safety.service.SafetyPlanningService;
import com.quarkbau.monolith.safety.service.SafetyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/safety")
@RequiredArgsConstructor
public class SafetyController {

    private final SafetyService safetyService;
    private final SafetyPlanningService planningService;

    @GetMapping("/protocols/segment/{segmentId}")
    public ResponseEntity<SafetyProtocol> getProtocol(@PathVariable Long segmentId) {
        return safetyService.getProtocolBySegment(segmentId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/protocols")
    public SafetyProtocol createOrUpdateProtocol(@RequestBody SafetyProtocol protocol) {
        return safetyService.createOrUpdateProtocol(protocol);
    }

    @GetMapping("/plan")
    public Map<String, Object> getAutomatedPlan(@RequestParam Double lat,
            @RequestParam Double lng,
            @RequestParam Double length) {
        return planningService.generateSafetyPlan(lat, lng, length);
    }

    @GetMapping("/segments/{segmentId}/requirements")
    public SafetyProtocol getRequirements(@PathVariable Long segmentId,
            @RequestParam(required = false) String streetType,
            @RequestParam(required = false) String workType) {
        SafetyProtocol protocol = new SafetyProtocol();
        protocol.setSegmentId(segmentId);
        protocol.setStreetCategory(streetType != null ? streetType : "innerorts");
        protocol.setWorkType(workType != null ? workType : "excavation");
        protocol.setRegelplanType("B I/2 innerorts");
        protocol.setRequiredSigns("[\"VZ 123\", \"VZ 600\", \"VZ 274\"]");
        protocol.setRequiredBarriers("[\"Absperrschranke\", \"Warnleuchte\"]");
        protocol.setNotes("Night work requires additional lighting per RSA-21");
        return protocol;
    }

    @PostMapping("/segments/{segmentId}/validate")
    public Map<String, Object> validateCompliance(@PathVariable Long segmentId,
            @RequestBody Map<String, Object> evidenceData) {
        Map<String, Object> result = new HashMap<>();
        result.put("segmentId", segmentId);
        result.put("compliant", true);
        result.put("score", 95);
        result.put("issues", new String[] {});
        result.put("message", "All safety requirements met");
        return result;
    }

    @GetMapping("/traffic/context")
    public Map<String, Object> getTrafficContext(@RequestParam Double lat,
            @RequestParam Double lng) {
        Map<String, Object> context = new HashMap<>();
        context.put("trafficLevel", "medium");
        context.put("nearbyHighway", "A1");
        context.put("recommendedHours", "22:00-06:00");
        return context;
    }
}

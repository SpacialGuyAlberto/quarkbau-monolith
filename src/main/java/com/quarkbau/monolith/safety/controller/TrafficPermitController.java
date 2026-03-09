package com.quarkbau.monolith.safety.controller;

import com.quarkbau.monolith.safety.model.TrafficPermit;
import com.quarkbau.monolith.safety.service.SafetyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/safety/permits")
@RequiredArgsConstructor
public class TrafficPermitController {

    private final SafetyService safetyService;

    @GetMapping("/segment/{segmentId}")
    public List<TrafficPermit> getPermits(@PathVariable Long segmentId) {
        return safetyService.getPermitsBySegment(segmentId);
    }

    @PostMapping
    public TrafficPermit createPermit(@RequestBody TrafficPermit permit) {
        return safetyService.createPermit(permit);
    }

    @PutMapping("/{id}/status")
    public TrafficPermit updateStatus(@PathVariable Long id, @RequestParam String status) {
        return safetyService.updatePermitStatus(id, status);
    }
}

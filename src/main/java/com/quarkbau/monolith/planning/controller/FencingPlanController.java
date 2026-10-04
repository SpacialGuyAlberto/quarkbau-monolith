package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.model.FencingPlan;
import com.quarkbau.monolith.planning.repository.FencingPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planning/fencing-plans")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FencingPlanController {

    private final FencingPlanRepository fencingPlanRepository;
    private final com.quarkbau.monolith.planning.repository.SegmentRepository segmentRepository;

    @GetMapping
    public ResponseEntity<List<FencingPlan>> getAllFencingPlans() {
        return ResponseEntity.ok(fencingPlanRepository.findAll());
    }

    @GetMapping("/segment/{segmentId}")
    public ResponseEntity<List<FencingPlan>> getFencingPlansBySegment(@PathVariable Long segmentId) {
        return ResponseEntity.ok(fencingPlanRepository.findBySegmentId(segmentId));
    }

    @PostMapping
    public ResponseEntity<FencingPlan> createFencingPlan(@RequestBody FencingPlan plan) {
        return ResponseEntity.ok(fencingPlanRepository.save(plan));
    }

    @PostMapping("/segment/{segmentId}/generate")
    public ResponseEntity<FencingPlan> generateStandardFencingPlan(
            @PathVariable Long segmentId, 
            @RequestParam(defaultValue = "standard") String strategy) {
        
        // This is a simplified generator.
        FencingPlan plan = new FencingPlan();
        plan.setSegmentId(segmentId);
        plan.setClosureType(strategy);
        
        java.util.List<com.quarkbau.monolith.planning.model.FencingElement> elements = new java.util.ArrayList<>();
        
        // We fetch the segment coordinates
        com.quarkbau.monolith.planning.model.Segment segment = segmentRepository.findById(segmentId).orElse(null);
        double baseLat = segment != null && segment.getStartLatitude() != null ? segment.getStartLatitude() : 52.5200;
        double baseLng = segment != null && segment.getStartLongitude() != null ? segment.getStartLongitude() : 13.4050;

        com.quarkbau.monolith.planning.model.FencingElement barrier = new com.quarkbau.monolith.planning.model.FencingElement();
        barrier.setElementType("BARRIER");
        barrier.setLatitude(baseLat);
        barrier.setLongitude(baseLng);
        elements.add(barrier);

        com.quarkbau.monolith.planning.model.FencingElement trafficLight = new com.quarkbau.monolith.planning.model.FencingElement();
        trafficLight.setElementType("TRAFFIC_LIGHT");
        trafficLight.setLatitude(baseLat + 0.0001); // Slightly offset
        trafficLight.setLongitude(baseLng + 0.0001);
        elements.add(trafficLight);

        com.quarkbau.monolith.planning.model.FencingElement cone = new com.quarkbau.monolith.planning.model.FencingElement();
        cone.setElementType("CONE");
        cone.setLatitude(baseLat - 0.0001); // Slightly offset
        cone.setLongitude(baseLng - 0.0001);
        elements.add(cone);
        
        plan.setElements(elements);
        return ResponseEntity.ok(fencingPlanRepository.save(plan));
    }
}

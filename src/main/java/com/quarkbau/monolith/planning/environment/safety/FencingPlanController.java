package com.quarkbau.monolith.planning.environment.safety;

import com.quarkbau.monolith.planning.segment.core.Segment;
import com.quarkbau.monolith.planning.segment.core.SegmentRepository;
import com.quarkbau.monolith.planning.environment.safety.FencingPlan;
import com.quarkbau.monolith.planning.environment.safety.FencingPlanRepository;
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
    private final com.quarkbau.monolith.planning.segment.core.SegmentRepository segmentRepository;

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
        
        java.util.List<com.quarkbau.monolith.planning.environment.safety.FencingElement> elements = new java.util.ArrayList<>();
        
        // We fetch the segment coordinates
        com.quarkbau.monolith.planning.segment.core.Segment segment = segmentRepository.findById(segmentId).orElse(null);
        double baseLat = segment != null && segment.getStartLatitude() != null ? segment.getStartLatitude() : 52.5200;
        double baseLng = segment != null && segment.getStartLongitude() != null ? segment.getStartLongitude() : 13.4050;

        com.quarkbau.monolith.planning.environment.safety.FencingElement barrier = new com.quarkbau.monolith.planning.environment.safety.FencingElement();
        barrier.setElementType("BARRIER");
        barrier.setLatitude(baseLat);
        barrier.setLongitude(baseLng);
        elements.add(barrier);

        com.quarkbau.monolith.planning.environment.safety.FencingElement trafficLight = new com.quarkbau.monolith.planning.environment.safety.FencingElement();
        trafficLight.setElementType("TRAFFIC_LIGHT");
        trafficLight.setLatitude(baseLat + 0.0001); // Slightly offset
        trafficLight.setLongitude(baseLng + 0.0001);
        elements.add(trafficLight);

        com.quarkbau.monolith.planning.environment.safety.FencingElement cone = new com.quarkbau.monolith.planning.environment.safety.FencingElement();
        cone.setElementType("CONE");
        cone.setLatitude(baseLat - 0.0001); // Slightly offset
        cone.setLongitude(baseLng - 0.0001);
        elements.add(cone);
        
        plan.setElements(elements);
        return ResponseEntity.ok(fencingPlanRepository.save(plan));
    }
}

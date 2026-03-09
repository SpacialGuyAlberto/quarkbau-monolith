package com.quarkbau.monolith.graph.controller;

import com.quarkbau.monolith.graph.model.SegmentNode;
import com.quarkbau.monolith.graph.service.ImpactAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final ImpactAnalysisService impactAnalysisService;

    @GetMapping("/segments/{id}/impact")
    public ResponseEntity<List<SegmentNode>> getSegmentImpact(@PathVariable Long id) {
        List<SegmentNode> impactedSegments = impactAnalysisService.calculateDelayImpact(id);
        return ResponseEntity.ok(impactedSegments);
    }

    @PostMapping("/segments/{id}/link/{targetId}")
    public ResponseEntity<String> linkSegments(@PathVariable Long id, @PathVariable Long targetId) {
        impactAnalysisService.linkSegments(id, targetId);
        return ResponseEntity.ok("Linked segment " + id + " -> " + targetId);
    }

    @GetMapping("/projects/{projectId}/risk")
    public ResponseEntity<Object> getProjectRisk(@PathVariable Long projectId) {
        return ResponseEntity.ok(java.util.Map.of(
                "projectId", projectId,
                "overallRiskLevel", "MEDIUM",
                "averageRiskScore", 65,
                "predictedDelayDays", 5));
    }

    @GetMapping("/projects/{projectId}/risk/details")
    public ResponseEntity<java.util.List<Object>> getProjectRiskDetails(@PathVariable Long projectId) {
        return ResponseEntity.ok(new java.util.ArrayList<>());
    }

    @PostMapping("/recalculate")
    public ResponseEntity<Void> recalculate() {
        // Stub for manually triggering risk recalculation
        return ResponseEntity.ok().build();
    }
}

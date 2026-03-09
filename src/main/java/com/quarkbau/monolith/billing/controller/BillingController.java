package com.quarkbau.monolith.billing.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/billing")
public class BillingController {

    @GetMapping("/worklogs")
    public ResponseEntity<List<Object>> getAllWorkLogs() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/worklogs/segment/{segmentId}")
    public ResponseEntity<List<Object>> getWorkLogsBySegment(@PathVariable Long segmentId) {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/reports/subcontractor/{subcontractorId}")
    public ResponseEntity<Object> getSubcontractorReport(
            @PathVariable Long subcontractorId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return ResponseEntity.ok(Map.of(
                "subcontractorId", subcontractorId,
                "totalAmount", 0.0));
    }
}

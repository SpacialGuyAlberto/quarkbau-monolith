package com.quarkbau.monolith.dashboard.controller;

import com.quarkbau.monolith.dashboard.dto.DashboardStatsDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @GetMapping("/stats")
    public ResponseEntity<DashboardStatsDTO> getStats() {
        // Mocking stats in the backend for now, but following the real contract
        DashboardStatsDTO stats = DashboardStatsDTO.builder()
                .overallProgress(68.5)
                .highRiskSegments(12)
                .highRiskTrend(5.2)
                .predictedDelayDays(3)
                .dailyProduction(450.0)
                .dailyProductionTarget(500.0)
                .openIncidents(4)
                .criticalIncidents(1)
                .pendingPermits(7)
                .avgPermitWaitDays(14)
                .build();

        return ResponseEntity.ok(stats);
    }
}

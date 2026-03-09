package com.quarkbau.monolith.dashboard.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardStatsDTO {
    private double overallProgress;
    private int highRiskSegments;
    private double highRiskTrend;
    private int predictedDelayDays;
    private double dailyProduction;
    private double dailyProductionTarget;
    private int openIncidents;
    private int criticalIncidents;
    private int pendingPermits;
    private int avgPermitWaitDays;
}

package com.quarkbau.monolith.planning.segment.core;

import com.quarkbau.monolith.planning.segment.core.GeometryPoint;
import com.quarkbau.monolith.planning.segment.workflow.WorkType;
import com.quarkbau.monolith.planning.segment.workflow.WorkflowState;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class SegmentDTO {
    private Long id;
    private Long startNvtId;
    private Long endNvtId;
    private Long connectedPopId;
    private Long assignedCrewId;
    private String streetName;
    private String streetType;
    private WorkType workType;
    private WorkflowState currentState;
    private Double length;
    private Double startLatitude;
    private Double startLongitude;
    private Double startElevation;
    private Double endLatitude;
    private Double endLongitude;
    private Double endElevation;
    private String startAddress;
    private String endAddress;
    private List<GeometryPoint> geometry;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
}

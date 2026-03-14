package com.quarkbau.monolith.planning.dto;

import com.quarkbau.monolith.planning.model.GeometryPoint;
import com.quarkbau.monolith.planning.model.WorkType;
import com.quarkbau.monolith.planning.model.WorkflowState;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class SegmentDTO {
    private Long id;
    private Long projectId;
    private Long assignedCrewId;
    private String streetName;
    private String streetType;
    private WorkType workType;
    private WorkflowState currentState;
    private Double length;
    private Double startLatitude;
    private Double startLongitude;
    private Double endLatitude;
    private Double endLongitude;
    private List<GeometryPoint> geometry;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
}

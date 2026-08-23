package com.quarkbau.monolith.planning.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.util.List;
import com.quarkbau.monolith.planning.model.GeometryPoint;
import com.quarkbau.monolith.planning.model.WorkType;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
public class ProjectDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long organizationId;
    private List<Long> segmentIds;
    private String organizationName;

    private List<GeometryPoint> geometry;

    @JsonProperty("lifecycle_todo")
    private List<WorkType> lifecycleTodo;

    @JsonProperty("lifecycle_done")
    private List<WorkType> lifecycleDone;
}
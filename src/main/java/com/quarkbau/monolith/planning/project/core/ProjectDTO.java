package com.quarkbau.monolith.planning.project.core;

import com.quarkbau.monolith.planning.project.cluster.ClusterDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import com.quarkbau.monolith.planning.segment.core.GeometryPoint;
import com.quarkbau.monolith.planning.segment.workflow.WorkType;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTO {

    private Long id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long organizationId;
    //private List<ClusterDTO> clusters;
    private String organizationName;

    private List<GeometryPoint> geometry;

    private Double latitude;
    private Double longitude;
    private Double startLatitude;
    private Double startLongitude;

    public Double getLatitude() {
        return latitude != null ? latitude : startLatitude;
    }

    public Double getLongitude() {
        return longitude != null ? longitude : startLongitude;
    }

    public Double getStartLatitude() {
        return startLatitude != null ? startLatitude : latitude;
    }

    public Double getStartLongitude() {
        return startLongitude != null ? startLongitude : longitude;
    }

    @JsonProperty("lifecycle_todo")
    private List<WorkType> lifecycleTodo;

    @JsonProperty("lifecycle_done")
    private List<WorkType> lifecycleDone;
}
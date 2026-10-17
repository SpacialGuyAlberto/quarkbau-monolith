package com.quarkbau.monolith.schedule.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quarkbau.monolith.schedule.model.AssignmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewAssignmentDTO {

    private Long id;

    @NotNull(message = "crewId is required")
    private Long crewId;

    private Long segmentId;

    private Long nodeId;
    private String nodeType;
    private String nodeName;

    @NotNull(message = "assignedDate is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate assignedDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm[:ss]")
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm[:ss]")
    private LocalTime endTime;

    private AssignmentStatus status;

    private String notes;

    // Display fields
    private String crewName;
    private String segmentName;
    private Long projectId;
}

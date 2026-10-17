package com.quarkbau.monolith.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quarkbau.monolith.planning.segment.core.Segment;
import com.quarkbau.monolith.planning.workforce.team.Crew;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "crew_assignments", indexes = {
    @Index(name = "idx_crew_assigned_date", columnList = "crew_id, assigned_date"),
    @Index(name = "idx_segment_assigned_date", columnList = "segment_id, assigned_date"),
    @Index(name = "idx_node_assigned_date", columnList = "node_id, node_type, assigned_date"),
    @Index(name = "idx_assignment_date", columnList = "assigned_date")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "crew_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Crew crew;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id", nullable = true)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Segment segment;

    @Column(name = "node_id")
    private Long nodeId;

    @Column(name = "node_type", length = 50)
    private String nodeType; // e.g. "NVT", "POP", "HUEP", "MUFFE"

    @Column(name = "node_name", length = 255)
    private String nodeName;

    @Column(name = "assigned_date", nullable = false)
    private LocalDate assignedDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    @Builder.Default
    private AssignmentStatus status = AssignmentStatus.SCHEDULED;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = AssignmentStatus.SCHEDULED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}

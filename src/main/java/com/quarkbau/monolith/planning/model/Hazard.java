package com.quarkbau.monolith.planning.model;

import com.quarkbau.monolith.auth.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "hazards")
public class Hazard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id", nullable = false)
    private Segment segment;

    @Column(name = "description")
    private String description;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by")
    private User reportedBy;

    @Enumerated(EnumType.STRING)
    private HazardStatus status;

    @Enumerated(EnumType.STRING)
    private HazardSeverityLevel severity;

    @Enumerated(EnumType.STRING)
    private HazardType type;

    @Enumerated(EnumType.STRING)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private LocalDateTime updatedAt;

    @Column(name = "photo_evidence_url")
    private String photoEvidenceUrl;

}

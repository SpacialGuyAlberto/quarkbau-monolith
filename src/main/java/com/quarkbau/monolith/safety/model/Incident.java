package com.quarkbau.monolith.safety.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "segment_id", nullable = false)
    private Long segmentId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "severity", nullable = false)
    private String severity;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "reported_by")
    private String reportedBy;

    @Column(name = "reported_at")
    private LocalDateTime reportedAt;

    @ElementCollection
    @CollectionTable(name = "incident_photos", joinColumns = @JoinColumn(name = "incident_id"))
    @Column(name = "photo_url")
    private List<String> photos;

    @PrePersist
    protected void onCreate() {
        reportedAt = LocalDateTime.now();
        if (status == null)
            status = "OPEN";
    }
}

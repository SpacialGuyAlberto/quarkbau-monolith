package com.quarkbau.monolith.gis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "gis_features")
@Getter
@Setter
public class GisFeature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "feature_type", nullable = false)
    private String featureType; // SEGMENT, NODE, MANHOLE, CABINET, SPLICE_POINT

    @Column(columnDefinition = "TEXT", nullable = false)
    private String geometry; // GeoJSON string

    @Column(nullable = false)
    private String version; // PLAN, AS_BUILT, CORRECTION

    @Column(nullable = false)
    private String status; // DRAFT, APPROVED, ARCHIVED

    @Column(columnDefinition = "TEXT")
    private String properties; // JSON string for additional properties

    @Column(name = "segment_id")
    private Long segmentId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}

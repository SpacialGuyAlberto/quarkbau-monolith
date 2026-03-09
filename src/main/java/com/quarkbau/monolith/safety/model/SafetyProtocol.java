package com.quarkbau.monolith.safety.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "safety_protocols")
public class SafetyProtocol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "segment_id", nullable = false)
    private Long segmentId;

    @Column(name = "regelplan_type")
    private String regelplanType;

    @Column(name = "street_category")
    private String streetCategory;

    @Column(name = "work_type")
    private String workType;

    @Column(name = "required_signs", columnDefinition = "TEXT")
    private String requiredSigns;

    @Column(name = "required_barriers", columnDefinition = "TEXT")
    private String requiredBarriers;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "validated")
    private Boolean validated = false;
}

package com.quarkbau.monolith.safety.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "traffic_permits")
public class TrafficPermit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "segment_id", nullable = false)
    private Long segmentId;

    @Column(name = "permit_number", nullable = false)
    private String permitNumber;

    @Column(name = "issuing_authority")
    private String issuingAuthority;

    @Column(name = "valid_from")
    private LocalDate validFrom;

    @Column(name = "valid_to")
    private LocalDate validTo;

    @Column(name = "status")
    private String status;

    @Column(name = "conditions", columnDefinition = "TEXT")
    private String conditions;
}

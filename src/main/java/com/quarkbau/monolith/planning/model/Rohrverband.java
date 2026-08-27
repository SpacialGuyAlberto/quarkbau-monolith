package com.quarkbau.monolith.planning.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "rohrverband")
@Data
public class Rohrverband {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // Ej: "24x7mm", "7x14mm"

    @Column(name = "color_code")
    private String colorCode;

    @Column(name = "passed_pressure_test")
    private Boolean passedPressureTest; // Fundamental para aprobar el "Einblasen" (soplado)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment", nullable = false)
    private Segment segment;
}

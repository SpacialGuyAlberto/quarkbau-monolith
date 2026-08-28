package com.quarkbau.monolith.planning.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


@Entity
@Table(name = "netzverteiler")
@Data
public class Netzverteiler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String identifier; // Ej: "NVt-45A"

    private Double latitude;
    private Double longitude;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pop_id")
    private Pop pop;


    @OneToMany(mappedBy = "nvt")
    private List<Huep> hueps;

}

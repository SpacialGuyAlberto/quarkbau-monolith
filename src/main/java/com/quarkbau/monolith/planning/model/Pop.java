package com.quarkbau.monolith.planning.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Table(name = "pop")
@Data
public class Pop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // Ej: "PoP-München-01"

    @Column(unique = true)
    private String identifier; // Ej: "POP-001"

    @Column(nullable = false)
    private String locationAddress;

    private Double latitude;
    private Double longitude;

    private Integer maxCapacityPorts; // Puertos totales disponibles

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cluster_id")
    private Cluster cluster;

    @OneToMany(mappedBy = "pop", cascade = CascadeType.ALL)
    private List<Netzverteiler> nvts;

    @OneToMany(mappedBy = "connectedPop", cascade = CascadeType.ALL)
    private List<Segment> directSegments;
}

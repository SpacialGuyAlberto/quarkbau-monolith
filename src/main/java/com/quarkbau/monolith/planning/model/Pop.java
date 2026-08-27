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

    @Column(nullable = false)
    private String locationAddress;

    private Integer maxCapacityPorts; // Puertos totales disponibles

    @OneToMany(mappedBy = "pop", cascade = CascadeType.ALL)
    private List<NetzVerteiler> nvts;
}

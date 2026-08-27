package com.quarkbau.monolith.planning.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;


@Entity
@Table(name = "netzverteiler")
@Data
public class NetzVerteiler {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String identifier; // Ej: "NVt-45A"

    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "netzVerteiler")
    private List<Muffe> hueps;

}

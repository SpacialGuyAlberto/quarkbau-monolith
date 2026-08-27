package com.quarkbau.monolith.planning.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "muffen")
@Data
public class Muffe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // Ej: "Haubenmuffe", "Muffe tipo DOMO"

    private Double latitude;
    private Double longitude;

    @Column(name = "splice_count")
    private Integer spliceCount; // Cantidad de fusiones realizadas

    @Column(name = "is_sealed")
    private Boolean isSealed; // Verificación de cierre hermético

    // Subcontratista que realizó la fusión
    @Column(name = "splicersubcontractor")
    private Long splicerSubcontractorId;
}

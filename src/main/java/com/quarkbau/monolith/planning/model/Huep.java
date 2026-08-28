package com.quarkbau.monolith.planning.model;


//HÜP (Hausübergabepunkt)
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "hueps")
@Data
public class Huep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String streetAddress;

    @Column(nullable = false)
    private String houseNumber;

    @Column(name = "installation_location")
    private String installationLocation; // "Keller" (Sótano), "Fassade" (Fachada)

    @Column(name = "status")
    private String status; // Ej: "INSTALLED", "PENDING_FUSION", "TESTED"

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "netzverteiler_id")
    private Netzverteiler nvt; // A qué armario está conectado este HÜP
}
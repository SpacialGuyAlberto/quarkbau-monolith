package com.quarkbau.monolith.inventory.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "machines")
@Data
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String serialNumber;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MachineStatus status;

    private String currentLocation;

    private Long assignedCrewId;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime lastMaintenanceDate;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = MachineStatus.AVAILABLE;
        }
    }
}

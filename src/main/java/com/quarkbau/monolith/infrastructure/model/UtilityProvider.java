package com.quarkbau.monolith.infrastructure.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "utility_providers")
public class UtilityProvider {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String contactEmail;
    private String serviceRegion;
}

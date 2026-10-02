package com.quarkbau.monolith.planning.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "external_utilities")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "utility_type", discriminatorType = DiscriminatorType.STRING)
public abstract class UtilityLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "geometry", columnDefinition = "jsonb")
    private List<GeometryPoint> geometry = new ArrayList<>();

    private Double depth;
    
    @Column(name = "operator_name")
    private String operatorName;
    
    @Column(name = "contact_phone")
    private String contactPhone;

    @Column(name = "hazard_level")
    private String hazardLevel; // e.g., LOW, MEDIUM, CRITICAL
    
    private String status; // e.g., ACTIVE, ABANDONED, PLANNED
}

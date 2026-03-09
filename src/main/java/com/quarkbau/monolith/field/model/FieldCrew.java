package com.quarkbau.monolith.field.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "field_crews")
public class FieldCrew {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String leader;
    private int members;
    private String currentProject;
    private String status;
    private String location;
}

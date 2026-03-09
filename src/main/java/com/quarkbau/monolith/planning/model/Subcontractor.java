package com.quarkbau.monolith.planning.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subcontractors")
public class Subcontractor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String contactPerson;
    private String email;
    private String phone;
    private Double rating;

    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    @ElementCollection
    @CollectionTable(name = "subcontractor_skills", joinColumns = @JoinColumn(name = "subcontractor_id"))
    @Column(name = "skill")
    private List<String> skills = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "subcontractor_certifications", joinColumns = @JoinColumn(name = "subcontractor_id"))
    @Column(name = "certification")
    private List<String> certifications = new ArrayList<>();

    private Double averageTimePerMeter;
    private Double defectRate;
    private Double reworkFrequency;

    @OneToMany(mappedBy = "subcontractor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Crew> crews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    public enum Status {
        PREFERRED, ACTIVE, BLOCKED
    }
}

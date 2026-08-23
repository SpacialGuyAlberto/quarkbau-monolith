package com.quarkbau.monolith.planning.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "projects")
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Segment> segments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "latitude")
    private Double startLatitude;

    @Column(name = "longitude")
    private Double startLongitude;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "geometry", columnDefinition = "jsonb")
    private List<GeometryPoint> geometry = new ArrayList<>();

    @JsonProperty("lifecycle_todo")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "lifecycle_todo", columnDefinition = "jsonb")
    private List<WorkType> lifecycleTodo = new ArrayList<>();

    @JsonProperty("lifecycle_done")
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "lifecycle_done", columnDefinition = "jsonb")
    private List<WorkType> lifecycleDone = new ArrayList<>();

}

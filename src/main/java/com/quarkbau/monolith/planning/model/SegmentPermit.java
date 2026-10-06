package com.quarkbau.monolith.planning.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@Entity
@Table(name = "segment_permits")
public class SegmentPermit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id", nullable = false)
    @JsonIgnore
    private Segment segment;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "pdf_url", nullable = false)
    private String pdfUrl;

    @Column(name = "preview_image_url")
    private String previewImageUrl;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

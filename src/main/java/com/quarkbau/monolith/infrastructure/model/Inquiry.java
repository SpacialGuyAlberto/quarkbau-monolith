package com.quarkbau.monolith.infrastructure.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "inquiries")
public class Inquiry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long projectId;

    @ManyToOne
    @JoinColumn(name = "utility_provider_id")
    private UtilityProvider utilityProvider;

    @Enumerated(EnumType.STRING)
    private InquiryStatus status;

    private LocalDate requestDate;
    private LocalDate responseDate;

    private String responseFileUrl;

    @Column(length = 2000)
    private String analysisResult;

    @Enumerated(EnumType.STRING)
    private AnalysisStatus analysisStatus = AnalysisStatus.NONE;

    public enum InquiryStatus {
        PENDING, SENT, RECEIVED_CLEAR, RECEIVED_CONFLICT
    }

    public enum AnalysisStatus {
        NONE, PENDING, COMPLETED, FAILED
    }
}

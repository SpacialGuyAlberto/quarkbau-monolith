package com.quarkbau.monolith.planning.dto;


import lombok.Data;

import java.util.List;

@Data
public class SubcontractorDTO {
    private Long id;
    private String name;
    private String contactName;
    private String phone;
    private String email;
    private Double rating;
    private String status;
    private List<String> skills;
    private List<String> certifications;
    private Double averageTimePerMeter;
    private Double defectRate;
    private Double reworkFrequency;
    private String organizationId;
}

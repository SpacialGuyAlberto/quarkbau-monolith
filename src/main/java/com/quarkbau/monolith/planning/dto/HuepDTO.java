package com.quarkbau.monolith.planning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HuepDTO {
    private Long id;
    private String identifier;
    private String streetAddress;
    private String houseNumber;
    private Double latitude;
    private Double longitude;
    private String installationLocation;
    private String status;
    private Long netzverteilerId;
}

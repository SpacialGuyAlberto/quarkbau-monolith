package com.quarkbau.monolith.planning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopDTO {
    private Long id;
    private String identifier;
    private String name;
    private String locationAddress;
    private Double latitude;
    private Double longitude;
    private Integer maxCapacityPorts;
    private Long clusterId;
    private List<NetzverteilerDTO> nvts;
}

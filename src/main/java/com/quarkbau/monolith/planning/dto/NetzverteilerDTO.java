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
public class NetzverteilerDTO {
    private Long id;
    private String identifier;
    private Double latitude;
    private Double longitude;
    private Long popId;
    private List<Long> huepIds;
}

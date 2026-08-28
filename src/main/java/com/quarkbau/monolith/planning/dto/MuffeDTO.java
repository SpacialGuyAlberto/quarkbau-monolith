package com.quarkbau.monolith.planning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MuffeDTO {
    private Long id;
    private String type;
    private Double latitude;
    private Double longitude;
    private Integer spliceCount;
    private Boolean isSealed;
    private Long splicerSubcontractorId;
}

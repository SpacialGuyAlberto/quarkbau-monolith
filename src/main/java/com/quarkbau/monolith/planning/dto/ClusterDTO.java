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
public class ClusterDTO {
    private Long id;
    private String name;
    private String description;
    private Long projectId;
    private Long projectManagerId;
    private List<PopDTO> pops;
}

package com.quarkbau.monolith.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidationResponseDTO {
    private boolean valid;
    @Builder.Default
    private List<String> errors = new ArrayList<>();
}

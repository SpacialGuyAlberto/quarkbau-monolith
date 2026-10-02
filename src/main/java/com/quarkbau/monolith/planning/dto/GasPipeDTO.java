package com.quarkbau.monolith.planning.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GasPipeDTO extends UtilityLineDTO {
    private String pressureClass;
    private String material;
}

package com.quarkbau.monolith.planning.environment.utility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WaterPipeDTO extends UtilityLineDTO {
    private String pipeType;
    private String material;
    private Integer diameterMm;
    private String pressureLevel;
}

package com.quarkbau.monolith.planning.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ElectricityCableDTO extends UtilityLineDTO {
    private String voltageCategory;
    private Integer voltageValue;
    private String shieldingType;
    private Integer numberOfCores;
}

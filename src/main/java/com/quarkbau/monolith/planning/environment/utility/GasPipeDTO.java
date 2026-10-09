package com.quarkbau.monolith.planning.environment.utility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GasPipeDTO extends UtilityLineDTO {
    private String pressureClass;
    private String material;
}

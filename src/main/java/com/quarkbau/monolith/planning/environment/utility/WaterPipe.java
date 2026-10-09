package com.quarkbau.monolith.planning.environment.utility;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("WATER")
public class WaterPipe extends UtilityLine {
    
    private String pipeType; // e.g., POTABLE, SEWAGE, RAINWATER
    private String material; // e.g., PVC, PE, CAST_IRON
    private Integer diameterMm;
    private String pressureLevel; // e.g., GRAVITY, PRESSURIZED
}

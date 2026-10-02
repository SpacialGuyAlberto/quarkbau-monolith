package com.quarkbau.monolith.planning.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("ELECTRICITY")
public class ElectricityCable extends UtilityLine {
    
    private String voltageCategory; // e.g., LOW_VOLTAGE, MEDIUM_VOLTAGE, HIGH_VOLTAGE
    private Integer voltageValue;
    private String shieldingType; // e.g., UNSHIELDED, STEEL_ARMORED
    private Integer numberOfCores;
}

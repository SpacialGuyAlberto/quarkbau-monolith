package com.quarkbau.monolith.planning.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("GAS")
public class GasPipe extends UtilityLine {
    
    private String pressureClass; // e.g., LOW_PRESSURE, MEDIUM_PRESSURE, HIGH_PRESSURE
    private String material; // e.g., STEEL, HDPE
}

package com.quarkbau.monolith.planning.environment.safety;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FencingElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // e.g. "BARRIER", "CONE", "SIGN_STOP", "TRAFFIC_LIGHT"
    private String elementType; 

    private Double latitude;
    private Double longitude;
    
    // Optional orientation of the element
    private Double rotation;
}

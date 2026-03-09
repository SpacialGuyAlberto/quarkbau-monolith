package com.quarkbau.monolith.planning.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeometryPoint {
    private Double lat;
    private Double lng;
    private Double z; // Elevation/Altitude
}

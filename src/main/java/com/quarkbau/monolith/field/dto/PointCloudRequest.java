package com.quarkbau.monolith.field.dto;

import lombok.Data;
import java.util.List;

@Data
public class PointCloudRequest {
    private Long segmentId;
    private List<Point3D> points;
    private double groundLevel; // Optional: can be inferred if null
}

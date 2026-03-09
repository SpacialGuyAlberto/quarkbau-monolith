package com.quarkbau.monolith.field.service;

import com.quarkbau.monolith.field.dto.Point3D;
import com.quarkbau.monolith.field.dto.PointCloudRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VolumetryService {

    /**
     * Calculates excavation volume using a simplified slicing/column method.
     * In a real-world scenario, we'd use more complex Voronoi or Delaunay
     * triangulation.
     */
    public double calculateVolume(PointCloudRequest request) {
        List<Point3D> points = request.getPoints();
        if (points == null || points.isEmpty()) {
            return 0.0;
        }

        final double finalGroundLevel = (request.getGroundLevel() != 0.0)
                ? request.getGroundLevel()
                : points.stream().mapToDouble(Point3D::getY).max().orElse(0.0);

        double minX = points.stream().mapToDouble(Point3D::getX).min().orElse(0.0);
        double maxX = points.stream().mapToDouble(Point3D::getX).max().orElse(0.0);
        double minZ = points.stream().mapToDouble(Point3D::getZ).min().orElse(0.0);
        double maxZ = points.stream().mapToDouble(Point3D::getZ).max().orElse(0.0);

        double width = maxX - minX;
        double depth = maxZ - minZ;
        double area = width * depth;

        double avgHeight = points.stream()
                .mapToDouble(p -> Math.max(0, finalGroundLevel - p.getY()))
                .average()
                .orElse(0.0);

        return area * avgHeight;
    }
}

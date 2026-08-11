package com.quarkbau.monolith.planning.dto.mappers;

import com.quarkbau.monolith.planning.dto.SegmentDTO;
import com.quarkbau.monolith.planning.model.Segment;
import com.quarkbau.monolith.planning.model.GeometryPoint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Coordinate;
import java.util.List;
import java.util.ArrayList;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SegmentMapper {

    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "assignedCrewId", target = "assignedCrew.id")
    Segment toEntity(SegmentDTO segmentDTO);

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "assignedCrew.id", target = "assignedCrewId")
    SegmentDTO toDto(Segment segment);

    default LineString map(List<GeometryPoint> value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        
        Coordinate[] coordinates = new Coordinate[value.size()];
        for (int i = 0; i < value.size(); i++) {
            GeometryPoint point = value.get(i);
            double z = point.getZ() != null ? point.getZ() : Double.NaN;
            coordinates[i] = new Coordinate(point.getLng(), point.getLat(), z);
        }
        
        // JTS requires LineString to have at least 2 points or 0 points
        if (coordinates.length == 1) {
            Coordinate[] validCoords = new Coordinate[2];
            validCoords[0] = coordinates[0];
            validCoords[1] = coordinates[0];
            coordinates = validCoords;
        }
        
        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createLineString(coordinates);
    }

    default List<GeometryPoint> map(LineString value) {
        if (value == null) {
            return null;
        }
        List<GeometryPoint> points = new ArrayList<>();
        for (Coordinate coord : value.getCoordinates()) {
            Double z = Double.isNaN(coord.getZ()) ? null : coord.getZ();
            points.add(new GeometryPoint(coord.y, coord.x, z));
        }
        return points;
    }
}

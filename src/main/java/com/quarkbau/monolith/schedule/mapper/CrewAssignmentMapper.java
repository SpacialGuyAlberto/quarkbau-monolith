package com.quarkbau.monolith.schedule.mapper;

import com.quarkbau.monolith.schedule.dto.CrewAssignmentDTO;
import com.quarkbau.monolith.schedule.model.CrewAssignment;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CrewAssignmentMapper {

    @Mapping(source = "crew.id", target = "crewId")
    @Mapping(source = "crew.name", target = "crewName")
    @Mapping(source = "segment.id", target = "segmentId")
    @Mapping(source = "segment.streetName", target = "segmentName")
    @Mapping(source = "segment.project.id", target = "projectId")
    @Mapping(source = "nodeId", target = "nodeId")
    @Mapping(source = "nodeType", target = "nodeType")
    @Mapping(source = "nodeName", target = "nodeName")
    CrewAssignmentDTO toDto(CrewAssignment assignment);

    List<CrewAssignmentDTO> toDtoList(List<CrewAssignment> assignments);

    @Mapping(source = "crewId", target = "crew.id")
    @Mapping(source = "segmentId", target = "segment.id")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CrewAssignment toEntity(CrewAssignmentDTO dto);

    @AfterMapping
    default void populateDisplayFields(CrewAssignment source, @MappingTarget CrewAssignmentDTO target) {
        if (target.getSegmentName() == null && source.getNodeName() != null) {
            String prefix = source.getNodeType() != null ? "[" + source.getNodeType() + "] " : "";
            target.setSegmentName(prefix + source.getNodeName());
        }
    }
}

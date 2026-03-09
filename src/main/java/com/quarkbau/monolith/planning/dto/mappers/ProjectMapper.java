package com.quarkbau.monolith.planning.dto.mappers;

import com.quarkbau.monolith.planning.dto.ProjectDTO;
import com.quarkbau.monolith.planning.model.Project;
import com.quarkbau.monolith.planning.model.Segment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    @Mapping(source = "segments", target = "segmentIds", qualifiedByName = "mapSegmentsToIds")
    ProjectDTO toDto(Project project);

    @Mapping(source = "organizationId", target = "organization.id")
    @Mapping(target = "segments", ignore = true) // Importante para evitar el error "Unknown property segments"
    Project toEntity(ProjectDTO projectDto);

    @Named("mapSegmentsToIds")
    default List<Long> mapSegmentsToIds(List<Segment> segments) {
        if (segments == null) return null;
        return segments.stream()
                .map(s -> s.getId()) // Cambiado a lambda simple para evitar problemas de visibilidad
                .collect(Collectors.toList());
    }
}
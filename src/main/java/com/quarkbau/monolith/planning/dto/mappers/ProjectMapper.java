package com.quarkbau.monolith.planning.dto.mappers;

import com.quarkbau.monolith.planning.dto.ProjectDTO;
import com.quarkbau.monolith.planning.model.Project;
import com.quarkbau.monolith.planning.model.Segment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {ClusterMapper.class}, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface ProjectMapper {

    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "organization.name", target = "organizationName")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source= "lifecycleTodo", target = "lifecycleTodo")
    @Mapping(source= "lifecycleDone", target = "lifecycleDone")
    ProjectDTO toDto(Project project);

    @Mapping(source = "organizationId", target = "organization.id")
    @Mapping(source= "lifecycleDone", target = "lifecycleDone")
    Project toEntity(ProjectDTO projectDto);
}
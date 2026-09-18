package com.quarkbau.monolith.planning.dto.mappers;

import com.quarkbau.monolith.planning.dto.ClusterDTO;
import com.quarkbau.monolith.planning.model.Cluster;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PopMapper.class})
public interface ClusterMapper {

    @Mapping(source = "project.id", target = "projectId")
    @Mapping(source = "projectManager.id", target = "projectManagerId")
    ClusterDTO toDto(Cluster cluster);

    @Mapping(source = "projectId", target = "project.id")
    @Mapping(source = "projectManagerId", target = "projectManager.id")
    Cluster toEntity(ClusterDTO clusterDto);
}

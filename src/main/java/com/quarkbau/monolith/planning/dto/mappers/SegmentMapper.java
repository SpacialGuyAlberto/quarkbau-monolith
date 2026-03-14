package com.quarkbau.monolith.planning.dto.mappers;


import com.quarkbau.monolith.planning.dto.SegmentDTO;
import com.quarkbau.monolith.planning.model.Segment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SegmentMapper {

    @Mapping(source = "projectId", target = "project.id")
    Segment toEntity(SegmentDTO segmentDTO);

    @Mapping(source= "project.id", target = "projectId")
    SegmentDTO toDto(Segment segment);

}

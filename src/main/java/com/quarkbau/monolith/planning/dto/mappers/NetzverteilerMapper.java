package com.quarkbau.monolith.planning.dto.mappers;

import com.quarkbau.monolith.planning.dto.NetzverteilerDTO;
import com.quarkbau.monolith.planning.model.Huep;
import com.quarkbau.monolith.planning.model.Netzverteiler;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface NetzverteilerMapper {

    @Mapping(source = "pop.id", target = "popId")
    @Mapping(source = "hueps", target = "huepIds", qualifiedByName = "mapHuepsToIds")
    @Mapping(source = "outgoingSegments", target = "outgoingSegmentIds", qualifiedByName = "mapSegmentsToIds")
    @Mapping(source = "incomingSegments", target = "incomingSegmentIds", qualifiedByName = "mapSegmentsToIds")
    NetzverteilerDTO toDto(Netzverteiler netzverteiler);

    @Mapping(source = "popId", target = "pop.id")
    @Mapping(target = "hueps", ignore = true)
    @Mapping(target = "outgoingSegments", ignore = true)
    @Mapping(target = "incomingSegments", ignore = true)
    Netzverteiler toEntity(NetzverteilerDTO dto);

    @Named("mapHuepsToIds")
    default List<Long> mapHuepsToIds(List<Huep> hueps) {
        if (hueps == null) return null;
        return hueps.stream()
                .map(Huep::getId)
                .collect(Collectors.toList());
    }

    @Named("mapSegmentsToIds")
    default List<Long> mapSegmentsToIds(List<com.quarkbau.monolith.planning.model.Segment> segments) {
        if (segments == null) return null;
        return segments.stream()
                .map(com.quarkbau.monolith.planning.model.Segment::getId)
                .collect(Collectors.toList());
    }
}

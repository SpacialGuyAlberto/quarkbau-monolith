package com.quarkbau.monolith.planning.dto.mappers;

import com.quarkbau.monolith.planning.dto.PopDTO;
import com.quarkbau.monolith.planning.model.Netzverteiler;
import com.quarkbau.monolith.planning.model.Pop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {NetzverteilerMapper.class}, unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface PopMapper {

    @Mapping(source = "cluster.id", target = "clusterId")
    PopDTO toDto(Pop pop);

    @Mapping(source = "clusterId", target = "cluster.id")
    Pop toEntity(PopDTO popDto);
}

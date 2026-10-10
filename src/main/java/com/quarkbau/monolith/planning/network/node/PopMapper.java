package com.quarkbau.monolith.planning.network.node;

import com.quarkbau.monolith.planning.network.node.NetzverteilerMapper;
import com.quarkbau.monolith.planning.network.node.PopDTO;
import com.quarkbau.monolith.planning.network.node.Netzverteiler;
import com.quarkbau.monolith.planning.network.node.Pop;
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

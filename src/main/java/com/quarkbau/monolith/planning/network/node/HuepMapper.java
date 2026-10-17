package com.quarkbau.monolith.planning.network.node;

import com.quarkbau.monolith.planning.network.node.HuepDTO;
import com.quarkbau.monolith.planning.network.node.Huep;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HuepMapper {

    @Mapping(source = "nvt.id", target = "netzverteilerId")
    HuepDTO toDto(Huep huep);

    @Mapping(source = "netzverteilerId", target = "nvt.id")
    Huep toEntity(HuepDTO dto);
}

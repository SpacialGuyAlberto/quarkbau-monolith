package com.quarkbau.monolith.planning.network.node;

import com.quarkbau.monolith.planning.network.node.MuffeDTO;
import com.quarkbau.monolith.planning.network.node.Muffe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MuffeMapper {

    MuffeDTO toDto(Muffe muffe);

    Muffe toEntity(MuffeDTO dto);
}

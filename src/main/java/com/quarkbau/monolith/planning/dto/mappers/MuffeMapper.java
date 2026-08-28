package com.quarkbau.monolith.planning.dto.mappers;

import com.quarkbau.monolith.planning.dto.MuffeDTO;
import com.quarkbau.monolith.planning.model.Muffe;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MuffeMapper {

    MuffeDTO toDto(Muffe muffe);

    Muffe toEntity(MuffeDTO dto);
}

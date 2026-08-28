package com.quarkbau.monolith.planning.dto.mappers;

import com.quarkbau.monolith.planning.dto.NetzverteilerDTO;
import com.quarkbau.monolith.planning.model.Huep;
import com.quarkbau.monolith.planning.model.Netzverteiler;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface NetzverteilerMapper {

    @Mapping(source = "pop.id", target = "popId")
    @Mapping(source = "hueps", target = "huepIds", qualifiedByName = "mapHuepsToIds")
    NetzverteilerDTO toDto(Netzverteiler netzverteiler);

    @Mapping(source = "popId", target = "pop.id")
    @Mapping(target = "hueps", ignore = true)
    Netzverteiler toEntity(NetzverteilerDTO dto);

    @Named("mapHuepsToIds")
    default List<Long> mapHuepsToIds(List<Huep> hueps) {
        if (hueps == null) return null;
        return hueps.stream()
                .map(Huep::getId)
                .collect(Collectors.toList());
    }
}

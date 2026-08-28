package com.quarkbau.monolith.planning.dto.mappers;

import com.quarkbau.monolith.planning.dto.PopDTO;
import com.quarkbau.monolith.planning.model.Netzverteiler;
import com.quarkbau.monolith.planning.model.Pop;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PopMapper {

    @Mapping(source = "nvts", target = "nvtIds", qualifiedByName = "mapNvtsToIds")
    PopDTO toDto(Pop pop);

    @Mapping(target = "nvts", ignore = true)
    Pop toEntity(PopDTO popDto);

    @Named("mapNvtsToIds")
    default List<Long> mapNvtsToIds(List<Netzverteiler> nvts) {
        if (nvts == null) return null;
        return nvts.stream()
                .map(Netzverteiler::getId)
                .collect(Collectors.toList());
    }
}

package com.quarkbau.monolith.planning.dto.mappers;

import com.quarkbau.monolith.planning.dto.*;
import com.quarkbau.monolith.planning.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UtilityLineMapper {

    default UtilityLine toEntity(UtilityLineDTO dto) {
        if (dto == null) return null;
        if (dto instanceof WaterPipeDTO) return toWaterPipe((WaterPipeDTO) dto);
        if (dto instanceof GasPipeDTO) return toGasPipe((GasPipeDTO) dto);
        if (dto instanceof ElectricityCableDTO) return toElectricityCable((ElectricityCableDTO) dto);
        throw new IllegalArgumentException("Unknown DTO type");
    }

    default UtilityLineDTO toDto(UtilityLine entity) {
        if (entity == null) return null;
        if (entity instanceof WaterPipe) return toWaterPipeDTO((WaterPipe) entity);
        if (entity instanceof GasPipe) return toGasPipeDTO((GasPipe) entity);
        if (entity instanceof ElectricityCable) return toElectricityCableDTO((ElectricityCable) entity);
        throw new IllegalArgumentException("Unknown Entity type");
    }

    WaterPipe toWaterPipe(WaterPipeDTO dto);
    GasPipe toGasPipe(GasPipeDTO dto);
    ElectricityCable toElectricityCable(ElectricityCableDTO dto);

    WaterPipeDTO toWaterPipeDTO(WaterPipe entity);
    GasPipeDTO toGasPipeDTO(GasPipe entity);
    ElectricityCableDTO toElectricityCableDTO(ElectricityCable entity);
}

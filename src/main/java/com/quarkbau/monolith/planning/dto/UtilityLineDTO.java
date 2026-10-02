package com.quarkbau.monolith.planning.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.quarkbau.monolith.planning.model.GeometryPoint;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "utilityType", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = WaterPipeDTO.class, name = "WATER"),
    @JsonSubTypes.Type(value = GasPipeDTO.class, name = "GAS"),
    @JsonSubTypes.Type(value = ElectricityCableDTO.class, name = "ELECTRICITY")
})
public abstract class UtilityLineDTO {
    private Long id;
    private String utilityType;
    private List<GeometryPoint> geometry;
    private Double depth;
    private String operatorName;
    private String contactPhone;
    private String hazardLevel;
    private String status;
}

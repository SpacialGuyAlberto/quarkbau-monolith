package com.quarkbau.monolith.planning.dto.mappers;

import com.quarkbau.monolith.planning.dto.SubcontractorDTO;
import com.quarkbau.monolith.planning.model.Subcontractor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;


@Mapper(componentModel = "spring")
public interface SubcontractorMapper {
    @Mapping(source = "organization.id", target = "organizationId")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "averageTimePerMeter", target = "averageTimePerMeter")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "certifications", target = "certifications")
    @Mapping(source = "skills", target = "skills")
    SubcontractorDTO toDto(Subcontractor subcontractor);


    @Mapping(source = "organizationId", target = "organization.id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "rating", target = "rating")
    @Mapping(source = "averageTimePerMeter", target = "averageTimePerMeter")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "certifications", target = "certifications")
    @Mapping(source = "skills", target = "skills")
    Subcontractor toEntity(SubcontractorDTO subcontractorDTO);

}

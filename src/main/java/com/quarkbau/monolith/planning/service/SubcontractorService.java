package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.planning.dto.SubcontractorDTO;
import com.quarkbau.monolith.planning.dto.mappers.SubcontractorMapper;
import com.quarkbau.monolith.planning.model.Organization;
import com.quarkbau.monolith.planning.model.Subcontractor;
import com.quarkbau.monolith.planning.repository.OrganizationRepository;
import com.quarkbau.monolith.planning.repository.SubcontractorRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubcontractorService {
    private final SubcontractorRepository subcontractorRepository;
    private final SubcontractorMapper subcontractorMapper;
    private final OrganizationRepository organizationRepository;

    @Transactional(value = "transactionManager", readOnly = true)
    public List<SubcontractorDTO> getAllSubcontractors(Long organizationId) {
        return subcontractorRepository.findByOrganizationId(organizationId).stream()
                .map(subcontractorMapper::toDto)
                .toList();
    }

    public SubcontractorDTO createSubcontractor(Long organizationId, SubcontractorDTO subcontractorDTO) {
        Subcontractor subcontractor = subcontractorMapper.toEntity(subcontractorDTO);

        Organization organization = organizationRepository.findById(organizationId).orElseThrow();

        subcontractor.setOrganization(organization);
        return subcontractorMapper.toDto(subcontractorRepository.save(subcontractor));
    }


}

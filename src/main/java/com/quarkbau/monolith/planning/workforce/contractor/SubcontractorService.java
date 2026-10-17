package com.quarkbau.monolith.planning.workforce.contractor;

import com.quarkbau.monolith.planning.workforce.contractor.SubcontractorDTO;
import com.quarkbau.monolith.planning.workforce.contractor.SubcontractorMapper;
import com.quarkbau.monolith.planning.workforce.contractor.Organization;
import com.quarkbau.monolith.planning.workforce.contractor.Subcontractor;
import com.quarkbau.monolith.planning.workforce.contractor.OrganizationRepository;
import com.quarkbau.monolith.planning.workforce.contractor.SubcontractorRepository;
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

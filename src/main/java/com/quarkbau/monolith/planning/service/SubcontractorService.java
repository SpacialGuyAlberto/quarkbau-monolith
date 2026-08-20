package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.planning.dto.SubcontractorDTO;
import com.quarkbau.monolith.planning.dto.mappers.SubcontractorMapper;
import com.quarkbau.monolith.planning.model.Subcontractor;
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

    @Transactional(value = "transactionManager", readOnly = true)
    public List<SubcontractorDTO> getAllSubcontractors() {
        return subcontractorRepository.findAll().stream()
                .map(subcontractorMapper::toDto)
                .toList();
    }


}

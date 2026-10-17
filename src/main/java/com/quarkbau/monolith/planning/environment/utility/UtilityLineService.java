package com.quarkbau.monolith.planning.environment.utility;

import com.quarkbau.monolith.planning.environment.utility.UtilityLineDTO;
import com.quarkbau.monolith.planning.environment.utility.UtilityLineMapper;
import com.quarkbau.monolith.planning.environment.utility.UtilityLine;
import com.quarkbau.monolith.planning.environment.utility.UtilityLineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UtilityLineService {

    private final UtilityLineRepository repository;
    private final UtilityLineMapper mapper;

    @Transactional(readOnly = true)
    public List<UtilityLineDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<UtilityLineDTO> getByType(String type) {
        return repository.findByUtilityType(type).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public UtilityLineDTO getById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("UtilityLine not found with id " + id));
    }

    @Transactional
    public UtilityLineDTO save(UtilityLineDTO dto) {
        UtilityLine entity = mapper.toEntity(dto);
        UtilityLine saved = repository.save(entity);
        return mapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}

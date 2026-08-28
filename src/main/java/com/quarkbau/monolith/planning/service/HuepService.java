package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.planning.dto.HuepDTO;
import com.quarkbau.monolith.planning.dto.mappers.HuepMapper;
import com.quarkbau.monolith.planning.model.Huep;
import com.quarkbau.monolith.planning.repository.HuepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HuepService {
    private final HuepRepository repository;
    private final HuepMapper mapper;

    public List<HuepDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public HuepDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    public HuepDTO save(HuepDTO dto) {
        Huep entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

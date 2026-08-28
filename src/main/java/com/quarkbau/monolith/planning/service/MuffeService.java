package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.planning.dto.MuffeDTO;
import com.quarkbau.monolith.planning.dto.mappers.MuffeMapper;
import com.quarkbau.monolith.planning.model.Muffe;
import com.quarkbau.monolith.planning.repository.MuffeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MuffeService {
    private final MuffeRepository repository;
    private final MuffeMapper mapper;

    public List<MuffeDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public MuffeDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    public MuffeDTO save(MuffeDTO dto) {
        Muffe entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

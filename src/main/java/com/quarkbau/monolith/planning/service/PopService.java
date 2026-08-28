package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.planning.dto.PopDTO;
import com.quarkbau.monolith.planning.dto.mappers.PopMapper;
import com.quarkbau.monolith.planning.model.Pop;
import com.quarkbau.monolith.planning.repository.PopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopService {
    private final PopRepository repository;
    private final PopMapper mapper;

    public List<PopDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public PopDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    public PopDTO save(PopDTO dto) {
        Pop entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

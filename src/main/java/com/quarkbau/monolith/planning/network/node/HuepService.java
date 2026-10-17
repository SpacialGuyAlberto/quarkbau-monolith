package com.quarkbau.monolith.planning.network.node;

import com.quarkbau.monolith.planning.network.node.HuepDTO;
import com.quarkbau.monolith.planning.network.node.HuepMapper;
import com.quarkbau.monolith.planning.network.node.Huep;
import com.quarkbau.monolith.planning.network.node.HuepRepository;
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

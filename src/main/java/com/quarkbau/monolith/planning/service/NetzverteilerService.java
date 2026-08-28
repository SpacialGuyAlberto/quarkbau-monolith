package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.planning.dto.NetzverteilerDTO;
import com.quarkbau.monolith.planning.dto.mappers.NetzverteilerMapper;
import com.quarkbau.monolith.planning.model.Netzverteiler;
import com.quarkbau.monolith.planning.repository.NetzverteilerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NetzverteilerService {
    private final NetzverteilerRepository repository;
    private final NetzverteilerMapper mapper;

    public List<NetzverteilerDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public NetzverteilerDTO findById(Long id) {
        return repository.findById(id)
                .map(mapper::toDto)
                .orElse(null);
    }

    public NetzverteilerDTO save(NetzverteilerDTO dto) {
        Netzverteiler entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

package com.quarkbau.monolith.planning.environment.safety;

import com.quarkbau.monolith.planning.environment.safety.Hazard;
import com.quarkbau.monolith.planning.environment.safety.HazardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HazardService {
    private final HazardRepository repository;

    public List<Hazard> findAll() {
        return repository.findAll();
    }

    public Hazard findById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    public Hazard save(Hazard entity) {
        return repository.save(entity);
    }

    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}

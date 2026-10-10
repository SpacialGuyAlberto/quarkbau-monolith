package com.quarkbau.monolith.planning.network.duct;

import com.quarkbau.monolith.planning.network.duct.Rohrverband;
import com.quarkbau.monolith.planning.network.duct.RohrverbandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RohrverbandService {
    private final RohrverbandRepository repository;

    public List<Rohrverband> findAll() {
        return repository.findAll();
    }

    public Rohrverband findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Rohrverband save(Rohrverband entity) {
        return repository.save(entity);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}

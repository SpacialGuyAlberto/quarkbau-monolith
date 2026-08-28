package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.model.Rohrverband;
import com.quarkbau.monolith.planning.service.RohrverbandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rohrverbands")
@RequiredArgsConstructor
public class RohrverbandController {

    private final RohrverbandService service;

    @GetMapping
    public ResponseEntity<List<Rohrverband>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rohrverband> getById(@PathVariable Long id) {
        Rohrverband entity = service.findById(id);
        if (entity == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(entity);
    }

    @PostMapping
    public ResponseEntity<Rohrverband> create(@RequestBody Rohrverband entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rohrverband> update(@PathVariable Long id, @RequestBody Rohrverband entity) {
        entity.setId(id);
        return ResponseEntity.ok(service.save(entity));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

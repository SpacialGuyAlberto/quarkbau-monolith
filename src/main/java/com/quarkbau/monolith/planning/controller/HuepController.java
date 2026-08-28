package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.dto.HuepDTO;
import com.quarkbau.monolith.planning.service.HuepService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hueps")
@RequiredArgsConstructor
public class HuepController {

    private final HuepService service;

    @GetMapping
    public ResponseEntity<List<HuepDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HuepDTO> getById(@PathVariable Long id) {
        HuepDTO dto = service.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<HuepDTO> create(@RequestBody HuepDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HuepDTO> update(@PathVariable Long id, @RequestBody HuepDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

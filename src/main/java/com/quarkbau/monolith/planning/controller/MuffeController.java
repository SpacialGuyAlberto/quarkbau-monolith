package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.dto.MuffeDTO;
import com.quarkbau.monolith.planning.service.MuffeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/muffes")
@RequiredArgsConstructor
public class MuffeController {

    private final MuffeService service;

    @GetMapping
    public ResponseEntity<List<MuffeDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MuffeDTO> getById(@PathVariable Long id) {
        MuffeDTO dto = service.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MuffeDTO> create(@RequestBody MuffeDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MuffeDTO> update(@PathVariable Long id, @RequestBody MuffeDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

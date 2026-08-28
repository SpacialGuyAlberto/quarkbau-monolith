package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.dto.PopDTO;
import com.quarkbau.monolith.planning.service.PopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pops")
@RequiredArgsConstructor
public class PopController {

    private final PopService service;

    @GetMapping
    public ResponseEntity<List<PopDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PopDTO> getById(@PathVariable Long id) {
        PopDTO dto = service.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<PopDTO> create(@RequestBody PopDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PopDTO> update(@PathVariable Long id, @RequestBody PopDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

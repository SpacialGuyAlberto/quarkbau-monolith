package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.dto.NetzverteilerDTO;
import com.quarkbau.monolith.planning.service.NetzverteilerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/netzverteilers")
@RequiredArgsConstructor
public class NetzverteilerController {

    private final NetzverteilerService service;

    @GetMapping
    public ResponseEntity<List<NetzverteilerDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NetzverteilerDTO> getById(@PathVariable Long id) {
        NetzverteilerDTO dto = service.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<NetzverteilerDTO> create(@RequestBody NetzverteilerDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NetzverteilerDTO> update(@PathVariable Long id, @RequestBody NetzverteilerDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.dto.UtilityLineDTO;
import com.quarkbau.monolith.planning.service.UtilityLineService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/utilities")
@RequiredArgsConstructor
public class UtilityLineController {

    private final UtilityLineService service;

    @GetMapping
    public ResponseEntity<List<UtilityLineDTO>> getAllUtilities(
            @RequestParam(required = false) String type) {
        if (type != null && !type.isEmpty()) {
            return ResponseEntity.ok(service.getByType(type.toUpperCase()));
        }
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/external")
    public ResponseEntity<List<UtilityLineDTO>> getExternalUtilities() {
        return ResponseEntity.ok(service.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilityLineDTO> getUtilityById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PostMapping
    public ResponseEntity<UtilityLineDTO> createUtility(@RequestBody UtilityLineDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilityLineDTO> updateUtility(@PathVariable Long id, @RequestBody UtilityLineDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtility(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

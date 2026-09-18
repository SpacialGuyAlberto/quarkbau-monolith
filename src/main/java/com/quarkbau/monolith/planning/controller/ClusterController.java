package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.dto.ClusterDTO;
import com.quarkbau.monolith.planning.service.ClusterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clusters")
@RequiredArgsConstructor
public class ClusterController {

    private final ClusterService service;

    @GetMapping
    public ResponseEntity<List<ClusterDTO>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<ClusterDTO>> getByProjectId(@PathVariable Long projectId) {
        return ResponseEntity.ok(service.findByProjectId(projectId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClusterDTO> getById(@PathVariable Long id) {
        ClusterDTO dto = service.findById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ClusterDTO> create(@RequestBody ClusterDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClusterDTO> update(@PathVariable Long id, @RequestBody ClusterDTO dto) {
        dto.setId(id);
        return ResponseEntity.ok(service.save(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

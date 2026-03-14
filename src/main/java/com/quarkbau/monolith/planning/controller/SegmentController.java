package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.dto.SegmentDTO;
import com.quarkbau.monolith.planning.service.SegmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SegmentController {

    private final SegmentService segmentService;

    @GetMapping("/projects/{projectId}/segments")
    public List<SegmentDTO> getProjectSegments(@PathVariable Long projectId) {
        return segmentService.findProjectSegments(projectId);
    }

    @PostMapping("/projects/{projectId}/segments")
    public ResponseEntity<SegmentDTO> createSegment(@PathVariable Long projectId, @RequestBody SegmentDTO segmentDTO) {
        return segmentService.createSegment(projectId, segmentDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/segments/{id}")
    public ResponseEntity<SegmentDTO> updateSegment(@PathVariable Long id, @RequestBody SegmentDTO segmentDTO) {
        return segmentService.updateSegment(id, segmentDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
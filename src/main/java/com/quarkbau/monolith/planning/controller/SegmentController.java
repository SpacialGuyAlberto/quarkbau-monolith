package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.dto.SegmentDTO;
import com.quarkbau.monolith.planning.model.Project;
import com.quarkbau.monolith.planning.model.Segment;
import com.quarkbau.monolith.planning.model.WorkType;
import com.quarkbau.monolith.planning.model.WorkflowState;
import com.quarkbau.monolith.planning.repository.ProjectRepository;
import com.quarkbau.monolith.planning.repository.SegmentRepository;
import com.quarkbau.monolith.planning.service.InventoryIntegrationService;
import com.quarkbau.monolith.graph.service.Neo4jSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SegmentController {

    private final SegmentRepository segmentRepository;
    private final ProjectRepository projectRepository;
    private final InventoryIntegrationService inventoryService;
    private final Neo4jSyncService neo4jSyncService;

    @GetMapping("/projects/{projectId}/segments")
    public List<SegmentDTO> getProjectSegments(@PathVariable Long projectId) {
        return segmentRepository.findByProject_Id(projectId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/projects/{projectId}/segments")
    public ResponseEntity<SegmentDTO> createSegment(@PathVariable Long projectId, @RequestBody SegmentDTO segmentDTO) {
        return projectRepository.findById(projectId)
                .map(project -> {
                    Segment segment = toEntity(segmentDTO, project);
                    Segment saved = segmentRepository.save(segment);
                    neo4jSyncService.syncSegment(saved);
                    return ResponseEntity.ok(toDTO(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/segments/{id}")
    public ResponseEntity<SegmentDTO> updateSegment(@PathVariable Long id, @RequestBody SegmentDTO segmentDTO) {
        return segmentRepository.findById(id)
                .map(existingSegment -> {
                    boolean isCompleting = !WorkflowState.COMPLETED.equals(existingSegment.getCurrentState())
                            && WorkflowState.COMPLETED.equals(segmentDTO.getCurrentState());

                    existingSegment.setCurrentState(segmentDTO.getCurrentState());
                    existingSegment.setWorkType(segmentDTO.getWorkType());
                    // ... other updates omitted for brevity in this initial migration step

                    Segment saved = segmentRepository.save(existingSegment);
                    neo4jSyncService.syncSegment(saved);

                    if (isCompleting && WorkType.DUCT_INSTALLATION.equals(saved.getWorkType())) {
                        double qty = saved.getLength() != null ? saved.getLength() * 1.05 : 0;
                        if (qty > 0) {
                            inventoryService.consumeMaterial("DUCT-40MM", qty);
                        }
                    }

                    return ResponseEntity.ok(toDTO(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private SegmentDTO toDTO(Segment segment) {
        SegmentDTO dto = new SegmentDTO();
        dto.setId(segment.getId());
        dto.setProjectId(segment.getProject() != null ? segment.getProject().getId() : null);
        dto.setAssignedCrewId(segment.getAssignedCrew() != null ? segment.getAssignedCrew().getId() : null);
        dto.setStreetName(segment.getStreetName());
        dto.setWorkType(segment.getWorkType());
        dto.setCurrentState(segment.getCurrentState());
        dto.setLength(segment.getLength());
        return dto;
    }

    private Segment toEntity(SegmentDTO dto, Project project) {
        Segment segment = new Segment();
        segment.setProject(project);
        segment.setStreetName(dto.getStreetName());
        segment.setWorkType(dto.getWorkType());
        segment.setCurrentState(dto.getCurrentState());
        segment.setLength(dto.getLength());
        return segment;
    }
}

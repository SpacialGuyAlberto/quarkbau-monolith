package com.quarkbau.monolith.field.controller;

import com.quarkbau.monolith.field.model.Evidence;
import com.quarkbau.monolith.field.repository.EvidenceRepository;
import com.quarkbau.monolith.field.service.VolumetryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/field")
@RequiredArgsConstructor
public class FieldController {

    private final EvidenceRepository evidenceRepository;
    private final VolumetryService volumetryService;

    @PostMapping("/evidence")
    public Evidence submitEvidence(@RequestBody Evidence evidence) {
        return evidenceRepository.save(evidence);
    }

    @GetMapping("/segments/{segmentId}/evidence")
    public List<Evidence> getEvidenceForSegment(@PathVariable Long segmentId) {
        return evidenceRepository.findBySegmentId(segmentId);
    }

    @PostMapping("/volumetry/calculate")
    public double calculateVolumetry(@RequestBody com.quarkbau.monolith.field.dto.PointCloudRequest request) {
        return volumetryService.calculateVolume(request);
    }
}

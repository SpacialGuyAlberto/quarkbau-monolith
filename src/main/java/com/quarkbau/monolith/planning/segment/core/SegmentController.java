package com.quarkbau.monolith.planning.segment.core;

import com.quarkbau.monolith.planning.segment.core.NearestSegmentDTO;
import com.quarkbau.monolith.planning.segment.core.SegmentDTO;
import com.quarkbau.monolith.planning.segment.core.SegmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.quarkbau.monolith.shared.base.BaseController;

@RestController
@RequestMapping("/api/segments")
public class SegmentController extends BaseController<SegmentDTO, Long> {

    private final SegmentService segmentService;

    public SegmentController(SegmentService segmentService) {
        super(segmentService);
        this.segmentService = segmentService;
    }

    @GetMapping("/crews/{crewId}/schedule")
    public ResponseEntity<List<SegmentDTO>> getCrewSchedule(@PathVariable Long crewId) {
        return ResponseEntity.ok(segmentService.findActiveSegmentsByCrewId(crewId));
    }

    // Project-specific segment methods have been moved to ProjectController

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<SegmentDTO> update(@PathVariable Long id, @RequestBody SegmentDTO segmentDTO) {
        return segmentService.update(id, segmentDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nearest")
    public List<NearestSegmentDTO> findNearestSegments(@RequestParam double lat, @RequestParam double lng, @RequestParam double radius) {
        return segmentService.findNearestSegments(lat, lng, radius);
    }

}
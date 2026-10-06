package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.model.SegmentPermit;
import com.quarkbau.monolith.planning.service.PermitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/segments/{segmentId}/permits")
@RequiredArgsConstructor
@CrossOrigin("*") // Para desarrollo local
public class PermitController {

    private final PermitService permitService;

    @PostMapping
    public ResponseEntity<SegmentPermit> uploadPermit(@PathVariable Long segmentId, 
                                                      @RequestParam("file") MultipartFile file) {
        try {
            SegmentPermit permit = permitService.uploadPermit(segmentId, file);
            return ResponseEntity.ok(permit);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SegmentPermit>> getPermits(@PathVariable Long segmentId) {
        List<SegmentPermit> permits = permitService.getPermitsForSegment(segmentId);
        return ResponseEntity.ok(permits);
    }
}

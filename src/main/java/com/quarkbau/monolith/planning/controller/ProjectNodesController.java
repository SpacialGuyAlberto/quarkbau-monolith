package com.quarkbau.monolith.planning.controller;

import com.quarkbau.monolith.planning.dto.NetzverteilerDTO;
import com.quarkbau.monolith.planning.dto.PopDTO;
import com.quarkbau.monolith.planning.service.NetzverteilerService;
import com.quarkbau.monolith.planning.service.PopService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Project-scoped node endpoints used by the Unity/XR client
 * (GET /api/projects/{projectId}/pops and /netzverteilers).
 */
@RestController
@RequestMapping("/api/projects/{projectId}")
@RequiredArgsConstructor
public class ProjectNodesController {

    private final PopService popService;
    private final NetzverteilerService netzverteilerService;

    @GetMapping("/pops")
    public ResponseEntity<List<PopDTO>> getPopsByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(popService.findByProjectId(projectId));
    }

    @GetMapping("/netzverteilers")
    public ResponseEntity<List<NetzverteilerDTO>> getNetzverteilersByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(netzverteilerService.findByProjectId(projectId));
    }
}

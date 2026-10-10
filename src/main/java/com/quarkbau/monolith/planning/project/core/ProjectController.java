package com.quarkbau.monolith.planning.project.core;

import com.quarkbau.monolith.planning.segment.core.Segment;
import com.quarkbau.monolith.planning.segment.core.SmartSegmentRecognitionService;
import com.quarkbau.monolith.planning.project.core.ProjectDTO;
import com.quarkbau.monolith.planning.project.core.ProjectMapper;
import com.quarkbau.monolith.planning.project.core.Project;
import com.quarkbau.monolith.planning.project.core.ProjectRepository;
import com.quarkbau.monolith.planning.project.core.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.quarkbau.monolith.shared.base.BaseController;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController extends BaseController<ProjectDTO, Long> {

    private final ProjectService service;
    private final ProjectRepository repository;
    private final ProjectMapper mapper;
    private final com.quarkbau.monolith.planning.segment.core.SmartSegmentRecognitionService smartSegmentRecognitionService;
    private final com.quarkbau.monolith.planning.segment.core.SegmentService segmentService;

    public ProjectController(ProjectService service, ProjectRepository repository, ProjectMapper mapper, com.quarkbau.monolith.planning.segment.core.SmartSegmentRecognitionService smartSegmentRecognitionService, com.quarkbau.monolith.planning.segment.core.SegmentService segmentService) {
        super(service);
        this.service = service;
        this.repository = repository;
        this.mapper = mapper;
        this.smartSegmentRecognitionService = smartSegmentRecognitionService;
        this.segmentService = segmentService;
    }

    @GetMapping("/{projectId}/segments")
    public ResponseEntity<java.util.List<com.quarkbau.monolith.planning.segment.core.SegmentDTO>> getProjectSegments(@PathVariable Long projectId) {
        return ResponseEntity.ok(segmentService.findProjectSegments(projectId));
    }

    @PostMapping("/{projectId}/segments")
    public ResponseEntity<com.quarkbau.monolith.planning.segment.core.SegmentDTO> createSegment(@PathVariable Long projectId, @RequestBody com.quarkbau.monolith.planning.segment.core.SegmentDTO segmentDTO) {
        return segmentService.createSegment(projectId, segmentDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    @GetMapping
    public ResponseEntity<List<ProjectDTO>> findAll() {
        List<ProjectDTO> projects = service.findAll();
        if (projects.isEmpty()) {
            Project p1 = new Project();
            p1.setName("Berlin Fiber Optics");
            p1.setDescription("Deploying fiber optics in Berlin Mitte");
            repository.save(p1);

            Project p2 = new Project();
            p2.setName("Munich 5G Expansion");
            p2.setDescription("Expanding 5G coverage in Munich");
            repository.save(p2);

            return ResponseEntity.ok(service.findAll());
        }
        return ResponseEntity.ok(projects);
    }

    @PostMapping("/{id}/planauskunft/process")
    public List<com.quarkbau.monolith.planning.segment.core.Segment> processPlanauskunft(
            @PathVariable Long id,
            @RequestParam("file") org.springframework.web.multipart.MultipartFile file,
            @RequestParam(value = "scale", defaultValue = "1.0") double scale,
            @RequestParam(value = "rotation", defaultValue = "0.0") double rotation,
            @RequestParam(value = "x", defaultValue = "0.0") double x,
            @RequestParam(value = "y", defaultValue = "0.0") double y,
            @RequestParam(value = "centerLat", defaultValue = "52.5200") double centerLat,
            @RequestParam(value = "centerLng", defaultValue = "13.4050") double centerLng) {
        return smartSegmentRecognitionService.processPlanauskunft(id, file, scale, rotation, x, y, centerLat, centerLng);
    }
}

package com.quarkbau.monolith.safety.controller;

import com.quarkbau.monolith.safety.model.Incident;
import com.quarkbau.monolith.safety.service.IncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/safety/incidents")
@RequiredArgsConstructor
public class IncidentController {

    private final IncidentService incidentService;

    @GetMapping
    public List<Incident> getAllIncidents() {
        return incidentService.getAllIncidents();
    }

    @GetMapping("/segment/{segmentId}")
    public List<Incident> getIncidentsBySegment(@PathVariable Long segmentId) {
        return incidentService.getIncidentsBySegment(segmentId);
    }

    @PostMapping
    public Incident reportIncident(@RequestBody Incident incident) {
        return incidentService.reportIncident(incident);
    }

    @PutMapping("/{id}/status")
    public Incident updateStatus(@PathVariable Long id, @RequestParam String status) {
        return incidentService.updateIncidentStatus(id, status);
    }
}

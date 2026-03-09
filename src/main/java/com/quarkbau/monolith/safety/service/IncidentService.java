package com.quarkbau.monolith.safety.service;

import com.quarkbau.monolith.safety.model.Incident;
import com.quarkbau.monolith.safety.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncidentService {

    private final IncidentRepository incidentRepository;

    public List<Incident> getAllIncidents() {
        return incidentRepository.findAll();
    }

    public List<Incident> getIncidentsBySegment(Long segmentId) {
        return incidentRepository.findBySegmentId(segmentId);
    }

    @Transactional
    public Incident reportIncident(Incident incident) {
        return incidentRepository.save(incident);
    }

    @Transactional
    public Incident updateIncidentStatus(Long id, String status) {
        Incident incident = incidentRepository.findById(id).orElseThrow();
        incident.setStatus(status);
        return incidentRepository.save(incident);
    }
}

package com.quarkbau.monolith.safety.service;

import com.quarkbau.monolith.safety.model.SafetyProtocol;
import com.quarkbau.monolith.safety.model.TrafficPermit;
import com.quarkbau.monolith.safety.repository.SafetyProtocolRepository;
import com.quarkbau.monolith.safety.repository.TrafficPermitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SafetyService {

    private final SafetyProtocolRepository protocolRepository;
    private final TrafficPermitRepository permitRepository;

    @Transactional
    public SafetyProtocol createOrUpdateProtocol(SafetyProtocol protocol) {
        Optional<SafetyProtocol> existing = protocolRepository.findBySegmentId(protocol.getSegmentId());
        if (existing.isPresent()) {
            SafetyProtocol update = existing.get();
            update.setRegelplanType(protocol.getRegelplanType());
            update.setStreetCategory(protocol.getStreetCategory());
            update.setWorkType(protocol.getWorkType());
            update.setRequiredSigns(protocol.getRequiredSigns());
            update.setRequiredBarriers(protocol.getRequiredBarriers());
            update.setNotes(protocol.getNotes());
            update.setValidated(protocol.getValidated());
            return protocolRepository.save(update);
        }
        return protocolRepository.save(protocol);
    }

    public Optional<SafetyProtocol> getProtocolBySegment(Long segmentId) {
        return protocolRepository.findBySegmentId(segmentId);
    }

    @Transactional
    public TrafficPermit createPermit(TrafficPermit permit) {
        if (permit.getStatus() == null) {
            permit.setStatus("PENDING");
        }
        return permitRepository.save(permit);
    }

    public List<TrafficPermit> getPermitsBySegment(Long segmentId) {
        return permitRepository.findBySegmentId(segmentId).map(List::of).orElse(List.of());
    }

    @Transactional
    public TrafficPermit updatePermitStatus(Long id, String status) {
        TrafficPermit permit = permitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permit not found"));
        permit.setStatus(status);
        return permitRepository.save(permit);
    }
}

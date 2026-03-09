package com.quarkbau.monolith.graph.service;

import com.quarkbau.monolith.graph.model.SegmentNode;
import com.quarkbau.monolith.graph.repository.SegmentNodeRepository;
import com.quarkbau.monolith.planning.model.Segment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class Neo4jSyncService {

    private final SegmentNodeRepository segmentNodeRepository;

    @Transactional
    public void syncSegment(Segment segment) {
        try {
            SegmentNode node = new SegmentNode(
                    segment.getId(),
                    segment.getStreetName() != null ? segment.getStreetName() : "Unnamed Segment",
                    segment.getWorkType() != null ? segment.getWorkType().name() : "UNKNOWN",
                    segment.getCurrentState() != null ? segment.getCurrentState().name() : "PLAN");

            segmentNodeRepository.save(node);
            log.info("Synchronized segment {} to Neo4j", segment.getId());
        } catch (Exception e) {
            log.error("Failed to sync segment {} to Neo4j: {}", segment.getId(), e.getMessage());
        }
    }
}

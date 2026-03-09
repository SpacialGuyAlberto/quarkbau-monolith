package com.quarkbau.monolith.graph.service;

import com.quarkbau.monolith.graph.model.SegmentNode;
import com.quarkbau.monolith.graph.repository.SegmentNodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImpactAnalysisService {

    private final SegmentNodeRepository segmentNodeRepository;

    /**
     * Calcula el impacto de un retraso en un segmento.
     * Navega recursivamente por las relaciones FOLLOWED_BY en Neo4j.
     */
    public List<SegmentNode> calculateDelayImpact(Long segmentId) {
        log.info("Calculating delay impact for segment {}", segmentId);
        List<SegmentNode> impacted = segmentNodeRepository.findImpactedSegments(segmentId);
        log.info("Found {} impacted segments for segment {}", impacted.size(), segmentId);
        return impacted;
    }

    @Transactional
    public void linkSegments(Long sourceId, Long targetId) {
        log.info("Linking segment {} to {}", sourceId, targetId);
        SegmentNode source = segmentNodeRepository.findById(sourceId).orElseThrow();
        SegmentNode target = segmentNodeRepository.findById(targetId).orElseThrow();
        source.getNextSegments().add(target);
        segmentNodeRepository.save(source);
    }
}

package com.quarkbau.monolith.graph.service;

import com.quarkbau.monolith.graph.model.SegmentNode;
import com.quarkbau.monolith.graph.model.PopNode;
import com.quarkbau.monolith.graph.model.NetzverteilerNode;
import com.quarkbau.monolith.graph.model.HeupNode;
import com.quarkbau.monolith.graph.repository.SegmentNodeRepository;
import com.quarkbau.monolith.graph.repository.PopNodeRepository;
import com.quarkbau.monolith.graph.repository.NetzverteilerNodeRepository;
import com.quarkbau.monolith.graph.repository.HeupNodeRepository;
import com.quarkbau.monolith.graph.repository.NetworkIntelligenceRepository;
import com.quarkbau.monolith.planning.model.Segment;
import com.quarkbau.monolith.planning.model.Pop;
import com.quarkbau.monolith.planning.model.Netzverteiler;
import com.quarkbau.monolith.planning.model.Heup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class Neo4jSyncService {

    private final SegmentNodeRepository segmentNodeRepository;
    private final PopNodeRepository popNodeRepository;
    private final NetzverteilerNodeRepository nvtNodeRepository;
    private final HeupNodeRepository heupNodeRepository;
    private final NetworkIntelligenceRepository intelligenceRepository;

    @Transactional("transactionManager")
    public void syncPop(Pop pop) {
        PopNode node = new PopNode(pop.getId(), pop.getName(), pop.getMaxCapacityPorts() != null ? pop.getMaxCapacityPorts().doubleValue() : 0.0);
        popNodeRepository.save(node);
        log.info("Synchronized POP {} to Neo4j", pop.getId());
    }

    @Transactional("transactionManager")
    public void syncNetzverteiler(Netzverteiler nvt) {
        NetzverteilerNode node = new NetzverteilerNode(nvt.getId(), nvt.getIdentifier(), 0, 0); // Assuming 0 for now
        nvtNodeRepository.save(node);
        log.info("Synchronized NVT {} to Neo4j", nvt.getId());
    }

    @Transactional("transactionManager")
    public void syncHeup(Heup heup) {
        HeupNode node = new HeupNode(heup.getId(), heup.getLocationAddress(), "PLANNED");
        heupNodeRepository.save(node);
        log.info("Synchronized HEUP {} to Neo4j", heup.getId());
    }

    @Transactional("transactionManager")
    public void syncSegment(Segment segment) {
        try {
            SegmentNode node = new SegmentNode(
                    segment.getId(),
                    segment.getStreetName() != null ? segment.getStreetName() : "Unnamed Segment",
                    segment.getWorkType() != null ? segment.getWorkType().name() : "UNKNOWN",
                    segment.getCurrentState() != null ? segment.getCurrentState().name() : "PLAN");

            segmentNodeRepository.save(node);
            
            // Link Start and End nodes (POP/NVT)
            Long startId = segment.getStartNvtId() != null ? segment.getStartNvtId() : segment.getConnectedPopId();
            Long endId = segment.getEndNvtId();
            
            if (startId != null && endId != null) {
                intelligenceRepository.connectNodes(startId, endId, segment.getId(), segment.getLength());
            }
            
            log.info("Synchronized segment {} to Neo4j", segment.getId());
        } catch (Exception e) {
            log.error("Failed to sync segment {} to Neo4j: {}", segment.getId(), e.getMessage());
        }
    }
}

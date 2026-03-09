package com.quarkbau.monolith.graph.repository;

import com.quarkbau.monolith.graph.model.SegmentNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SegmentNodeRepository extends Neo4jRepository<SegmentNode, Long> {

    @Query("MATCH (start:Segment {id: $segmentId})-[:FOLLOWED_BY*1..]->(impacted:Segment) RETURN impacted")
    List<SegmentNode> findImpactedSegments(Long segmentId);
}

package com.quarkbau.monolith.graph.repository;

import com.quarkbau.monolith.graph.model.NetzverteilerNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetzverteilerNodeRepository extends Neo4jRepository<NetzverteilerNode, Long> {
}

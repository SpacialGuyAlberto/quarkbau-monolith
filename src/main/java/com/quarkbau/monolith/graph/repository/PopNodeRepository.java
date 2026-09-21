package com.quarkbau.monolith.graph.repository;

import com.quarkbau.monolith.graph.model.PopNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PopNodeRepository extends Neo4jRepository<PopNode, Long> {
}

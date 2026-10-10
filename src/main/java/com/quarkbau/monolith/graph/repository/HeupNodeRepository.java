package com.quarkbau.monolith.graph.repository;

import com.quarkbau.monolith.graph.model.HeupNode;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeupNodeRepository extends Neo4jRepository<HeupNode, Long> {
}

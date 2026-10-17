package com.quarkbau.monolith.planning.network.node;

import com.quarkbau.monolith.planning.network.node.Huep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HuepRepository extends JpaRepository<Huep, Long> {
}

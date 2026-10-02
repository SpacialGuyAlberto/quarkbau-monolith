package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.UtilityLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UtilityLineRepository extends JpaRepository<UtilityLine, Long> {
    List<UtilityLine> findByUtilityType(String utilityType);
}

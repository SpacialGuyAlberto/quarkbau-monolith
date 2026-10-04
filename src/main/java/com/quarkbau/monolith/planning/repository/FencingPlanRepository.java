package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.FencingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FencingPlanRepository extends JpaRepository<FencingPlan, Long> {
    List<FencingPlan> findBySegmentId(Long segmentId);
}

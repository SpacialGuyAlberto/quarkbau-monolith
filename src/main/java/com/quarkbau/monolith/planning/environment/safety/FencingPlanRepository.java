package com.quarkbau.monolith.planning.environment.safety;

import com.quarkbau.monolith.planning.environment.safety.FencingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FencingPlanRepository extends JpaRepository<FencingPlan, Long> {
    List<FencingPlan> findBySegmentId(Long segmentId);
}

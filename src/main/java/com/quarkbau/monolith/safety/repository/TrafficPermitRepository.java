package com.quarkbau.monolith.safety.repository;

import com.quarkbau.monolith.safety.model.TrafficPermit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TrafficPermitRepository extends JpaRepository<TrafficPermit, Long> {
    Optional<TrafficPermit> findBySegmentId(Long segmentId);
}

package com.quarkbau.monolith.safety.repository;

import com.quarkbau.monolith.safety.model.SafetyProtocol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SafetyProtocolRepository extends JpaRepository<SafetyProtocol, Long> {
    Optional<SafetyProtocol> findBySegmentId(Long segmentId);
}

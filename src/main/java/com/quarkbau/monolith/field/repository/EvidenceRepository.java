package com.quarkbau.monolith.field.repository;

import com.quarkbau.monolith.field.model.Evidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EvidenceRepository extends JpaRepository<Evidence, Long> {
    List<Evidence> findBySegmentId(Long segmentId);
}

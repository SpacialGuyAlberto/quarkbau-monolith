package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.SegmentPermit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SegmentPermitRepository extends JpaRepository<SegmentPermit, UUID> {
    List<SegmentPermit> findBySegmentId(Long segmentId);
}

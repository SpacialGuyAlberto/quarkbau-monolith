package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {
    List<Segment> findByProject_Id(Long projectId);
}

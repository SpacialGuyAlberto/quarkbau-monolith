package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.Subcontractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcontractorRepository extends JpaRepository<Subcontractor, Long> {
}

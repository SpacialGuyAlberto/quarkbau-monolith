package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.Crew;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrewRepository extends JpaRepository<Crew, Long> {
}

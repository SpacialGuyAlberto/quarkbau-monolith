package com.quarkbau.monolith.planning.environment.safety;

import com.quarkbau.monolith.planning.environment.safety.Hazard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HazardRepository extends JpaRepository<Hazard, UUID> {
}

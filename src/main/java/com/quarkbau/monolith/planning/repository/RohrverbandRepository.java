package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.Rohrverband;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RohrverbandRepository extends JpaRepository<Rohrverband, Long> {
}

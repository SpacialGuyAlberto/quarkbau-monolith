package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.Muffe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MuffeRepository extends JpaRepository<Muffe, Long> {
}

package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.Netzverteiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetzverteilerRepository extends JpaRepository<Netzverteiler, Long> {
}

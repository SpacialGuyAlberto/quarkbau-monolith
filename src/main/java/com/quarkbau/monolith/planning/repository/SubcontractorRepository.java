package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.Subcontractor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubcontractorRepository extends JpaRepository<Subcontractor, Long> {

    List<Subcontractor> findAll();
}

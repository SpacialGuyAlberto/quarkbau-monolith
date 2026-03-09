package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    Optional<Project> findByName(String name);
    Optional<Project> findById(Long id);
    Optional<Project> findByNameIgnoreCase(String name);
}

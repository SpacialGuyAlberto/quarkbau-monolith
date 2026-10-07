package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.Pop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PopRepository extends JpaRepository<Pop, Long> {
    List<Pop> findByClusterId(Long clusterId);

    @org.springframework.data.jpa.repository.Query("SELECT p FROM Pop p WHERE p.cluster.project.id = :projectId")
    List<Pop> findByProjectId(@org.springframework.data.repository.query.Param("projectId") Long projectId);
}

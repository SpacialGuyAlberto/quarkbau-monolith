package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.Netzverteiler;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NetzverteilerRepository extends JpaRepository<Netzverteiler, Long> {

    @org.springframework.data.jpa.repository.Query("SELECT n FROM Netzverteiler n WHERE n.pop.cluster.project.id = :projectId")
    java.util.List<Netzverteiler> findByProjectId(@org.springframework.data.repository.query.Param("projectId") Long projectId);
}

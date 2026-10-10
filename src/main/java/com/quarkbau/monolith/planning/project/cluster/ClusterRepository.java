package com.quarkbau.monolith.planning.project.cluster;

import com.quarkbau.monolith.planning.project.cluster.Cluster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClusterRepository extends JpaRepository<Cluster, Long> {
    List<Cluster> findByProjectId(Long projectId);
}

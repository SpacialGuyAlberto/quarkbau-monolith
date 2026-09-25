package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.model.Pop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PopRepository extends JpaRepository<Pop, Long> {
    List<Pop> findByClusterId(Long clusterId);
}

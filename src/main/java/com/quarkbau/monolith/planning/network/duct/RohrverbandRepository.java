package com.quarkbau.monolith.planning.network.duct;

import com.quarkbau.monolith.planning.network.duct.Rohrverband;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RohrverbandRepository extends JpaRepository<Rohrverband, Long> {
}

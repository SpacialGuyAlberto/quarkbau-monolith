package com.quarkbau.monolith.inventory.repository;

import com.quarkbau.monolith.inventory.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {
    List<Material> findByQuantityOnHandLessThanEqual(Double target);
}

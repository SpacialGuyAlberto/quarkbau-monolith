package com.quarkbau.monolith.infrastructure.repository;

import com.quarkbau.monolith.infrastructure.model.UtilityProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UtilityProviderRepository extends JpaRepository<UtilityProvider, Long> {
    List<UtilityProvider> findByServiceRegion(String serviceRegion);
}

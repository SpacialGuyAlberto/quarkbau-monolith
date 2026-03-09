package com.quarkbau.monolith.gis.repository;

import com.quarkbau.monolith.gis.model.GisFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GisFeatureRepository extends JpaRepository<GisFeature, Long> {
    List<GisFeature> findByVersion(String version);

    List<GisFeature> findByVersionAndFeatureType(String version, String featureType);

    long countByVersion(String version);
}

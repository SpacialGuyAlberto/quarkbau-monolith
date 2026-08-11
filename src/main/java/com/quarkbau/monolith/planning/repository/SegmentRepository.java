package com.quarkbau.monolith.planning.repository;

import com.quarkbau.monolith.planning.dto.NearestSegmentDTO;
import com.quarkbau.monolith.planning.model.GeometryPoint;
import com.quarkbau.monolith.planning.model.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {
    List<Segment> findByProject_Id(Long projectId);

    @Query(value = """
        SELECT s.id,
               s.street_name as streetName,
               ST_AsGeoJSON(s.geometry) as positionGeoJson,
               ST_Distance(
                   s.geometry::geography,
                   ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography
               ) as distanceToUser
        FROM segments s
        WHERE ST_DWithin(
            s.geometry::geography,
            ST_SetSRID(ST_MakePoint(:lng, :lat), 4326)::geography,
            :radiusMeters
        )
        ORDER BY distanceToUser ASC
        """, nativeQuery = true)
    List<NearestSegmentDTO> findNearbySegments(
            @Param("lat") double lat,
            @Param("lng") double lng,
            @Param("radiusMeters") double radiusMeters
    );

}

package com.quarkbau.monolith.planning.segment.core;

import com.quarkbau.monolith.planning.segment.workflow.WorkflowState;
import com.quarkbau.monolith.planning.segment.core.NearestSegmentDTO;
import com.quarkbau.monolith.planning.segment.core.GeometryPoint;
import com.quarkbau.monolith.planning.segment.core.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SegmentRepository extends JpaRepository<Segment, Long> {
    @Query("SELECT s FROM Segment s WHERE s.project.id = :projectId")
    List<Segment> findByProjectId(@Param("projectId") Long projectId);

    @Query("SELECT s FROM Segment s WHERE s.assignedCrew.id = :crewId AND s.currentState != com.quarkbau.monolith.planning.segment.workflow.WorkflowState.COMPLETED")
    List<Segment> findActiveSegmentsByCrewId(@Param("crewId") Long crewId);

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

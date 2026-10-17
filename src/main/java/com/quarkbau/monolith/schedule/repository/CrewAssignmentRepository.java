package com.quarkbau.monolith.schedule.repository;

import com.quarkbau.monolith.schedule.model.AssignmentStatus;
import com.quarkbau.monolith.schedule.model.CrewAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CrewAssignmentRepository extends JpaRepository<CrewAssignment, Long> {

    @Query("SELECT ca FROM CrewAssignment ca " +
           "JOIN FETCH ca.crew c " +
           "LEFT JOIN FETCH ca.segment s " +
           "LEFT JOIN FETCH s.project p " +
           "WHERE ca.crew.id = :crewId AND ca.assignedDate = :assignedDate")
    List<CrewAssignment> findByCrewIdAndAssignedDate(
            @Param("crewId") Long crewId,
            @Param("assignedDate") LocalDate assignedDate);

    @Query("SELECT ca FROM CrewAssignment ca " +
           "JOIN FETCH ca.crew c " +
           "LEFT JOIN FETCH ca.segment s " +
           "LEFT JOIN FETCH s.project p " +
           "WHERE ca.segment.id = :segmentId " +
           "ORDER BY ca.assignedDate ASC")
    List<CrewAssignment> findBySegmentId(@Param("segmentId") Long segmentId);

    @Query("SELECT ca FROM CrewAssignment ca " +
           "JOIN FETCH ca.crew c " +
           "WHERE ca.nodeId = :nodeId AND ca.nodeType = :nodeType " +
           "ORDER BY ca.assignedDate ASC")
    List<CrewAssignment> findByNodeIdAndNodeType(
            @Param("nodeId") Long nodeId,
            @Param("nodeType") String nodeType);

    @Query("SELECT ca FROM CrewAssignment ca " +
           "JOIN FETCH ca.crew c " +
           "LEFT JOIN FETCH ca.segment s " +
           "LEFT JOIN FETCH s.project p " +
           "WHERE ca.crew.id = :crewId AND ca.assignedDate BETWEEN :start AND :end " +
           "ORDER BY ca.assignedDate ASC, ca.startTime ASC")
    List<CrewAssignment> findByCrewIdAndAssignedDateBetween(
            @Param("crewId") Long crewId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    @Query("SELECT ca FROM CrewAssignment ca " +
           "JOIN FETCH ca.crew c " +
           "JOIN ca.segment s " +
           "JOIN s.project p " +
           "WHERE p.id = :projectId AND ca.assignedDate BETWEEN :start AND :end " +
           "ORDER BY ca.assignedDate ASC, ca.startTime ASC")
    List<CrewAssignment> findByProjectIdAndAssignedDateBetween(
            @Param("projectId") Long projectId,
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    @Query("SELECT ca FROM CrewAssignment ca " +
           "JOIN FETCH ca.crew c " +
           "LEFT JOIN FETCH ca.segment s " +
           "LEFT JOIN FETCH s.project p " +
           "WHERE ca.assignedDate BETWEEN :start AND :end " +
           "ORDER BY ca.assignedDate ASC, ca.startTime ASC")
    List<CrewAssignment> findByAssignedDateBetween(
            @Param("start") LocalDate start,
            @Param("end") LocalDate end);

    @Query("SELECT ca FROM CrewAssignment ca " +
           "WHERE ca.crew.id = :crewId AND ca.assignedDate = :assignedDate " +
           "AND (:excludeId IS NULL OR ca.id != :excludeId) AND ca.status != 'CANCELLED'")
    List<CrewAssignment> findConflictingAssignments(
            @Param("crewId") Long crewId,
            @Param("assignedDate") LocalDate assignedDate,
            @Param("excludeId") Long excludeId);

    boolean existsBySegmentIdAndAssignedDate(Long segmentId, LocalDate assignedDate);

    boolean existsByNodeIdAndNodeTypeAndAssignedDate(Long nodeId, String nodeType, LocalDate assignedDate);

    @Modifying
    @Query("DELETE FROM CrewAssignment ca WHERE ca.segment.id = :segmentId AND ca.status = :status")
    void deleteBySegmentIdAndStatus(@Param("segmentId") Long segmentId, @Param("status") AssignmentStatus status);

    @Modifying
    @Query("DELETE FROM CrewAssignment ca WHERE ca.nodeId = :nodeId AND ca.nodeType = :nodeType AND ca.status = :status")
    void deleteByNodeIdAndNodeTypeAndStatus(@Param("nodeId") Long nodeId, @Param("nodeType") String nodeType, @Param("status") AssignmentStatus status);
}

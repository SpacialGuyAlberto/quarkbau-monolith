package com.quarkbau.monolith.planning.service;

import com.quarkbau.monolith.graph.service.Neo4jSyncService;
import com.quarkbau.monolith.planning.dto.NearestSegmentDTO;
import com.quarkbau.monolith.planning.dto.SegmentDTO;
import com.quarkbau.monolith.planning.dto.mappers.SegmentMapper;
import com.quarkbau.monolith.planning.model.Project;
import com.quarkbau.monolith.planning.model.Segment;
import com.quarkbau.monolith.planning.model.WorkType;
import com.quarkbau.monolith.planning.model.WorkflowState;
import com.quarkbau.monolith.planning.repository.ProjectRepository;
import com.quarkbau.monolith.planning.repository.SegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class  SegmentService {

    private final SegmentRepository segmentRepository;
    private final ProjectRepository projectRepository;
    private final InventoryIntegrationService inventoryService;
    private final Neo4jSyncService neo4jSyncService;
    private final SegmentMapper segmentMapper;
    private final EntityManager entityManager;


    @Transactional(value = "transactionManager", readOnly = true) // <-- FUERZA ESTO
    public List<SegmentDTO> findProjectSegments(Long projectId) {
        return segmentRepository.findByProjectId(projectId).stream()
                .map(segmentMapper::toDto)
                .collect(Collectors.toList());
    }


    @Transactional("transactionManager") // <-- FUERZA ESTO
    public Optional<SegmentDTO> createSegment(Long projectId, SegmentDTO segmentDTO) {
        return projectRepository.findById(projectId).map(project -> {
            Segment segment = segmentMapper.toEntity(segmentDTO);
            segment.setProject(project);
            
            // Fix transient references by loading proxies
            if (segment.getStartNvt() != null && segment.getStartNvt().getId() != null) {
                segment.setStartNvt(entityManager.getReference(com.quarkbau.monolith.planning.model.Netzverteiler.class, segment.getStartNvt().getId()));
            } else {
                segment.setStartNvt(null);
            }
            if (segment.getEndNvt() != null && segment.getEndNvt().getId() != null) {
                segment.setEndNvt(entityManager.getReference(com.quarkbau.monolith.planning.model.Netzverteiler.class, segment.getEndNvt().getId()));
            } else {
                segment.setEndNvt(null);
            }
            if (segment.getConnectedPop() != null && segment.getConnectedPop().getId() != null) {
                segment.setConnectedPop(entityManager.getReference(com.quarkbau.monolith.planning.model.Pop.class, segment.getConnectedPop().getId()));
            } else {
                segment.setConnectedPop(null);
            }
            if (segment.getAssignedCrew() != null && segment.getAssignedCrew().getId() != null) {
                segment.setAssignedCrew(entityManager.getReference(com.quarkbau.monolith.planning.model.Crew.class, segment.getAssignedCrew().getId()));
            } else {
                segment.setAssignedCrew(null);
            }
            
            Segment saved = segmentRepository.save(segment);
            neo4jSyncService.syncSegment(saved);
            return segmentMapper.toDto(saved);
        });
    }


    @Transactional("transactionManager") // <-- FUERZA ESTO
    public Optional<SegmentDTO> updateSegment(Long id, SegmentDTO segmentDTO) {
        return segmentRepository.findById(id).map(existingSegment -> {
            boolean isCompleting = !WorkflowState.COMPLETED.equals(existingSegment.getCurrentState())
                    && WorkflowState.COMPLETED.equals(segmentDTO.getCurrentState());

            existingSegment.setCurrentState(segmentDTO.getCurrentState());
            existingSegment.setWorkType(segmentDTO.getWorkType());
            existingSegment.setStreetName(segmentDTO.getStreetName());
            existingSegment.setLength(segmentDTO.getLength());
            
            if (segmentDTO.getGeometry() != null && !segmentDTO.getGeometry().isEmpty()) {
                existingSegment.setGeometry(segmentDTO.getGeometry());
            }

            Segment saved = segmentRepository.save(existingSegment);
            neo4jSyncService.syncSegment(saved);

            if (isCompleting && WorkType.DUCT_INSTALLATION.equals(saved.getWorkType())) {
                double qty = saved.getLength() != null ? saved.getLength() * 1.05 : 0;
                if (qty > 0) {
                    inventoryService.consumeMaterial("DUCT-40MM", qty);
                }
            }
            return segmentMapper.toDto(saved);
        });
    }

    public List<NearestSegmentDTO> findNearestSegments(double lat, double lng, double radius) {
        List<NearestSegmentDTO> nearestSegments = segmentRepository.findNearbySegments(lat, lng, radius);
        return nearestSegments;
    }


}
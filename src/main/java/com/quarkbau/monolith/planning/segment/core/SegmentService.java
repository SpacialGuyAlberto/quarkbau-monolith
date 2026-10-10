package com.quarkbau.monolith.planning.segment.core;

import com.quarkbau.monolith.planning.network.node.Netzverteiler;
import com.quarkbau.monolith.planning.network.node.Pop;
import com.quarkbau.monolith.planning.shared.integration.InventoryIntegrationService;
import com.quarkbau.monolith.planning.workforce.team.Crew;
import com.quarkbau.monolith.graph.service.Neo4jSyncService;
import com.quarkbau.monolith.planning.segment.core.NearestSegmentDTO;
import com.quarkbau.monolith.planning.segment.core.SegmentDTO;
import com.quarkbau.monolith.planning.segment.core.SegmentMapper;
import com.quarkbau.monolith.planning.workforce.team.CrewBusyException;
import com.quarkbau.monolith.planning.project.core.Project;
import com.quarkbau.monolith.planning.segment.core.Segment;
import com.quarkbau.monolith.planning.segment.workflow.WorkType;
import com.quarkbau.monolith.planning.segment.workflow.WorkflowState;
import com.quarkbau.monolith.planning.project.core.ProjectRepository;
import com.quarkbau.monolith.planning.segment.core.SegmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityManager;
import com.quarkbau.monolith.shared.base.BaseServiceImpl;

@Service
public class SegmentService extends BaseServiceImpl<Segment, SegmentDTO, Long> {

    private final SegmentRepository segmentRepository;
    private final ProjectRepository projectRepository;
    private final InventoryIntegrationService inventoryService;
    private final Neo4jSyncService neo4jSyncService;
    private final SegmentMapper segmentMapper;
    private final EntityManager entityManager;
    private final org.springframework.context.ApplicationEventPublisher eventPublisher;

    public SegmentService(SegmentRepository segmentRepository, SegmentMapper segmentMapper,
                          ProjectRepository projectRepository, InventoryIntegrationService inventoryService,
                          Neo4jSyncService neo4jSyncService, EntityManager entityManager,
                          org.springframework.context.ApplicationEventPublisher eventPublisher) {
        super(segmentRepository, segmentMapper);
        this.segmentRepository = segmentRepository;
        this.segmentMapper = segmentMapper;
        this.projectRepository = projectRepository;
        this.inventoryService = inventoryService;
        this.neo4jSyncService = neo4jSyncService;
        this.entityManager = entityManager;
        this.eventPublisher = eventPublisher;
    }


    @Transactional(value = "transactionManager", readOnly = true)
    public List<SegmentDTO> findProjectSegments(Long projectId) {
        return segmentRepository.findByProjectId(projectId).stream()
                .map(segmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(value = "transactionManager", readOnly = true)
    public List<SegmentDTO> findActiveSegmentsByCrewId(Long crewId) {
        return segmentRepository.findActiveSegmentsByCrewId(crewId).stream()
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
                segment.setStartNvt(entityManager.getReference(com.quarkbau.monolith.planning.network.node.Netzverteiler.class, segment.getStartNvt().getId()));
            } else {
                segment.setStartNvt(null);
            }
            if (segment.getEndNvt() != null && segment.getEndNvt().getId() != null) {
                segment.setEndNvt(entityManager.getReference(com.quarkbau.monolith.planning.network.node.Netzverteiler.class, segment.getEndNvt().getId()));
            } else {
                segment.setEndNvt(null);
            }
            if (segment.getConnectedPop() != null && segment.getConnectedPop().getId() != null) {
                segment.setConnectedPop(entityManager.getReference(com.quarkbau.monolith.planning.network.node.Pop.class, segment.getConnectedPop().getId()));
            } else {
                segment.setConnectedPop(null);
            }
            if (segment.getAssignedCrew() != null && segment.getAssignedCrew().getId() != null) {
                Long crewId = segment.getAssignedCrew().getId();
                List<Segment> activeSegments;
                
                if (segment.getPlannedStartDate() != null && segment.getPlannedEndDate() != null) {
                    activeSegments = segmentRepository.findConflictingSegmentsByCrewIdAndDates(
                        crewId, segment.getPlannedStartDate(), segment.getPlannedEndDate());
                } else {
                    activeSegments = segmentRepository.findActiveSegmentsByCrewId(crewId);
                }
                
                if (!activeSegments.isEmpty()) {
                    java.time.LocalDate nextAvailable = activeSegments.stream()
                        .map(Segment::getPlannedEndDate)
                        .filter(java.util.Objects::nonNull)
                        .max(java.time.LocalDate::compareTo)
                        .orElse(null);

                    String message = "The crew is currently busy or scheduled on another segment during these dates.";
                    if (nextAvailable != null) {
                        message = "The crew is currently busy during these dates. Next available date: " + nextAvailable.plusDays(1) + ".";
                    }
                    throw new CrewBusyException(message, crewId);
                }
                segment.setAssignedCrew(entityManager.getReference(com.quarkbau.monolith.planning.workforce.team.Crew.class, crewId));
            } else {
                segment.setAssignedCrew(null);
            }
            
            Segment saved = segmentRepository.save(segment);
            neo4jSyncService.syncSegment(saved);
            return segmentMapper.toDto(saved);
        });
    }


    @Override
    @Transactional("transactionManager") // <-- FUERZA ESTO
    public Optional<SegmentDTO> update(Long id, SegmentDTO segmentDTO) {
        return segmentRepository.findById(id).map(existingSegment -> {
            boolean isCompleting = !WorkflowState.COMPLETED.equals(existingSegment.getCurrentState())
                    && WorkflowState.COMPLETED.equals(segmentDTO.getCurrentState());

            if (segmentDTO.getAssignedCrewId() != null) {
                Long newCrewId = segmentDTO.getAssignedCrewId();
                Long currentCrewId = (existingSegment.getAssignedCrew() != null) ? existingSegment.getAssignedCrew().getId() : null;
                
                boolean datesChanged = false;
                if (segmentDTO.getPlannedStartDate() != null && segmentDTO.getPlannedEndDate() != null) {
                    if (!segmentDTO.getPlannedStartDate().equals(existingSegment.getPlannedStartDate()) ||
                        !segmentDTO.getPlannedEndDate().equals(existingSegment.getPlannedEndDate())) {
                        datesChanged = true;
                    }
                }
                
                if (!newCrewId.equals(currentCrewId) || datesChanged) {
                    List<Segment> activeSegments;
                    if (segmentDTO.getPlannedStartDate() != null && segmentDTO.getPlannedEndDate() != null) {
                        activeSegments = segmentRepository.findConflictingSegmentsByCrewIdAndDates(
                            newCrewId, segmentDTO.getPlannedStartDate(), segmentDTO.getPlannedEndDate());
                    } else {
                        activeSegments = segmentRepository.findActiveSegmentsByCrewId(newCrewId);
                    }
                    
                    // Filter out the current segment
                    activeSegments = activeSegments.stream()
                        .filter(s -> !s.getId().equals(existingSegment.getId()))
                        .collect(Collectors.toList());

                    if (!activeSegments.isEmpty()) {
                        java.time.LocalDate nextAvailable = activeSegments.stream()
                            .map(Segment::getPlannedEndDate)
                            .filter(java.util.Objects::nonNull)
                            .max(java.time.LocalDate::compareTo)
                            .orElse(null);

                        String message = "The crew is currently busy or scheduled on another segment during these dates.";
                        if (nextAvailable != null) {
                            message = "The crew is currently busy during these dates. Next available date: " + nextAvailable.plusDays(1) + ".";
                        }
                        throw new CrewBusyException(message, newCrewId);
                    }
                    existingSegment.setAssignedCrew(entityManager.getReference(com.quarkbau.monolith.planning.workforce.team.Crew.class, newCrewId));
                }
            } else {
                existingSegment.setAssignedCrew(null);
            }

            boolean stateChanged = !segmentDTO.getCurrentState().equals(existingSegment.getCurrentState());
            existingSegment.setCurrentState(segmentDTO.getCurrentState());
            existingSegment.setWorkType(segmentDTO.getWorkType());
            existingSegment.setStreetName(segmentDTO.getStreetName());
            existingSegment.setLength(segmentDTO.getLength());
            
            existingSegment.setPlannedStartDate(segmentDTO.getPlannedStartDate());
            existingSegment.setPlannedEndDate(segmentDTO.getPlannedEndDate());
            
            if (segmentDTO.getGeometry() != null && !segmentDTO.getGeometry().isEmpty()) {
                existingSegment.setGeometry(segmentDTO.getGeometry());
            }

            Segment saved = segmentRepository.save(existingSegment);
            neo4jSyncService.syncSegment(saved);

            if (stateChanged) {
                eventPublisher.publishEvent(new com.quarkbau.monolith.planning.segment.workflow.WorkflowStateChangedEvent(this, saved));
            }

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
package com.quarkbau.monolith.schedule.service;

import com.quarkbau.monolith.planning.segment.core.Segment;
import com.quarkbau.monolith.planning.segment.core.SegmentRepository;
import com.quarkbau.monolith.planning.workforce.team.Crew;
import com.quarkbau.monolith.planning.workforce.team.CrewRepository;
import com.quarkbau.monolith.schedule.dto.CrewAssignmentDTO;
import com.quarkbau.monolith.schedule.dto.ValidationResponseDTO;
import com.quarkbau.monolith.schedule.mapper.CrewAssignmentMapper;
import com.quarkbau.monolith.schedule.model.AssignmentStatus;
import com.quarkbau.monolith.schedule.model.CrewAssignment;
import com.quarkbau.monolith.schedule.repository.CrewAssignmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final CrewAssignmentRepository assignmentRepository;
    private final CrewRepository crewRepository;
    private final SegmentRepository segmentRepository;
    private final CrewAssignmentMapper assignmentMapper;

    /**
     * Valida una asignación según las reglas de negocio
     */
    @Transactional(readOnly = true)
    public ValidationResponseDTO validateAssignment(CrewAssignmentDTO dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Assignment data must not be null");
            return new ValidationResponseDTO(false, errors);
        }

        if (dto.getAssignedDate() == null) {
            errors.add("Assigned date is required (each assignment corresponds to one day)");
        }

        // 1. Verificación de cuadrilla
        Crew crew = null;
        if (dto.getCrewId() == null) {
            errors.add("Crew ID is required");
        } else {
            crew = crewRepository.findById(dto.getCrewId()).orElse(null);
            if (crew == null) {
                errors.add("Crew with ID " + dto.getCrewId() + " not found");
            }
        }

        // 2. Verificación de destino (Segmento o Nodo)
        Segment segment = null;
        if (dto.getSegmentId() == null && dto.getNodeId() == null) {
            errors.add("Either segmentId or nodeId is required");
        } else if (dto.getSegmentId() != null) {
            segment = segmentRepository.findById(dto.getSegmentId()).orElse(null);
            if (segment == null) {
                errors.add("Segment with ID " + dto.getSegmentId() + " not found");
            }
        }

        // 3. Verificación de doble asignación para la cuadrilla en ese día
        if (dto.getCrewId() != null && dto.getAssignedDate() != null) {
            List<CrewAssignment> conflicts = assignmentRepository.findConflictingAssignments(
                    dto.getCrewId(),
                    dto.getAssignedDate(),
                    dto.getId()
            );

            if (!conflicts.isEmpty()) {
                String crewName = crew != null ? crew.getName() : "Crew #" + dto.getCrewId();
                errors.add("Crew '" + crewName + "' is already assigned to another segment or node on " + dto.getAssignedDate());
            }
        }

        // 4. Verificación de fechas planificadas del tramo (si aplica)
        if (segment != null && dto.getAssignedDate() != null) {
            if (segment.getPlannedStartDate() != null && dto.getAssignedDate().isBefore(segment.getPlannedStartDate())) {
                errors.add("Assignment date (" + dto.getAssignedDate() + 
                        ") is before segment's planned start date (" + segment.getPlannedStartDate() + ")");
            }
            if (segment.getPlannedEndDate() != null && dto.getAssignedDate().isAfter(segment.getPlannedEndDate())) {
                errors.add("Assignment date (" + dto.getAssignedDate() + 
                        ") is after segment's planned end date (" + segment.getPlannedEndDate() + ")");
            }
        }

        // 5. Verificación de ventana horaria
        if (dto.getStartTime() != null && dto.getEndTime() != null) {
            if (!dto.getEndTime().isAfter(dto.getStartTime())) {
                errors.add("End time (" + dto.getEndTime() + ") must be after start time (" + dto.getStartTime() + ")");
            }
        }

        return new ValidationResponseDTO(errors.isEmpty(), errors);
    }

    /**
     * Sincroniza las asignaciones diarias de una cuadrilla para todo el intervalo de un segmento
     */
    @Transactional
    public void syncSegmentAssignments(Segment segment) {
        if (segment == null || segment.getId() == null) {
            return;
        }

        // Si no tiene cuadrilla o rango de fechas, eliminar asignaciones planificadas existentes
        if (segment.getAssignedCrew() == null || segment.getPlannedStartDate() == null || segment.getPlannedEndDate() == null) {
            assignmentRepository.deleteBySegmentIdAndStatus(segment.getId(), AssignmentStatus.SCHEDULED);
            return;
        }

        Crew crew = segment.getAssignedCrew();
        LocalDate start = segment.getPlannedStartDate();
        LocalDate end = segment.getPlannedEndDate();

        if (start.isAfter(end)) {
            log.warn("Segment {} has plannedStartDate after plannedEndDate", segment.getId());
            return;
        }

        // Obtener asignaciones existentes para este segmento
        List<CrewAssignment> existing = assignmentRepository.findBySegmentId(segment.getId());

        // Limpiar asignaciones que caigan fuera del nuevo rango (solo las que estén en SCHEDULED)
        for (CrewAssignment ca : existing) {
            if (ca.getStatus() == AssignmentStatus.SCHEDULED) {
                if (ca.getAssignedDate().isBefore(start) || ca.getAssignedDate().isAfter(end)) {
                    assignmentRepository.delete(ca);
                }
            }
        }

        // Crear o actualizar asignación para cada día del intervalo
        List<CrewAssignment> toSave = new ArrayList<>();
        String segmentDesc = segment.getStreetName() != null ? segment.getStreetName() : "Segment #" + segment.getId();

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            final LocalDate currentDate = date;
            CrewAssignment assignment = existing.stream()
                    .filter(ca -> ca.getAssignedDate().equals(currentDate))
                    .findFirst()
                    .orElse(null);

            if (assignment == null) {
                // Verificar que la cuadrilla no esté ocupada en otro lugar ese día
                List<CrewAssignment> conflicts = assignmentRepository.findConflictingAssignments(crew.getId(), currentDate, null);
                if (conflicts.isEmpty()) {
                    CrewAssignment ca = CrewAssignment.builder()
                            .crew(crew)
                            .segment(segment)
                            .assignedDate(currentDate)
                            .status(AssignmentStatus.SCHEDULED)
                            .notes("Scheduled work on " + segmentDesc)
                            .build();
                    toSave.add(ca);
                } else {
                    log.warn("Crew {} already has an assignment on {}, skipping auto-create for segment {}",
                            crew.getId(), currentDate, segment.getId());
                }
            } else {
                // Actualizar cuadrilla si cambió
                assignment.setCrew(crew);
                toSave.add(assignment);
            }
        }

        if (!toSave.isEmpty()) {
            assignmentRepository.saveAll(toSave);
            log.info("Saved {} daily assignments for segment {} from {} to {}",
                    toSave.size(), segment.getId(), start, end);
        }
    }

    /**
     * Sincroniza las asignaciones diarias de una cuadrilla para todo el intervalo de un nodo (PoP, NVT, etc.)
     */
    @Transactional
    public void syncNodeAssignments(Long crewId, Long nodeId, String nodeType, String nodeName, LocalDate start, LocalDate end) {
        if (nodeId == null || nodeType == null) {
            return;
        }

        if (crewId == null || start == null || end == null) {
            assignmentRepository.deleteByNodeIdAndNodeTypeAndStatus(nodeId, nodeType, AssignmentStatus.SCHEDULED);
            return;
        }

        if (start.isAfter(end)) {
            log.warn("Node {} has start date after end date", nodeId);
            return;
        }

        Crew crew = crewRepository.findById(crewId).orElse(null);
        if (crew == null) {
            log.warn("Crew {} not found for node assignment", crewId);
            return;
        }

        List<CrewAssignment> existing = assignmentRepository.findByNodeIdAndNodeType(nodeId, nodeType);

        for (CrewAssignment ca : existing) {
            if (ca.getStatus() == AssignmentStatus.SCHEDULED) {
                if (ca.getAssignedDate().isBefore(start) || ca.getAssignedDate().isAfter(end)) {
                    assignmentRepository.delete(ca);
                }
            }
        }

        List<CrewAssignment> toSave = new ArrayList<>();
        String name = nodeName != null ? nodeName : nodeType + " #" + nodeId;

        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            final LocalDate currentDate = date;
            CrewAssignment assignment = existing.stream()
                    .filter(ca -> ca.getAssignedDate().equals(currentDate))
                    .findFirst()
                    .orElse(null);

            if (assignment == null) {
                List<CrewAssignment> conflicts = assignmentRepository.findConflictingAssignments(crewId, currentDate, null);
                if (conflicts.isEmpty()) {
                    CrewAssignment ca = CrewAssignment.builder()
                            .crew(crew)
                            .nodeId(nodeId)
                            .nodeType(nodeType)
                            .nodeName(name)
                            .assignedDate(currentDate)
                            .status(AssignmentStatus.SCHEDULED)
                            .notes("Installation/Maintenance on [" + nodeType + "] " + name)
                            .build();
                    toSave.add(ca);
                } else {
                    log.warn("Crew {} already has an assignment on {}, skipping auto-create for node {}",
                            crewId, currentDate, nodeId);
                }
            } else {
                assignment.setCrew(crew);
                assignment.setNodeName(name);
                toSave.add(assignment);
            }
        }

        if (!toSave.isEmpty()) {
            assignmentRepository.saveAll(toSave);
            log.info("Saved {} daily assignments for node [{}] {} from {} to {}",
                    toSave.size(), nodeType, nodeId, start, end);
        }
    }

    /**
     * Crea una asignación manual (un día)
     */
    @Transactional
    public CrewAssignmentDTO createAssignment(CrewAssignmentDTO dto) {
        ValidationResponseDTO validation = validateAssignment(dto);
        if (!validation.isValid()) {
            throw new IllegalArgumentException(String.join(", ", validation.getErrors()));
        }

        Crew crew = crewRepository.findById(dto.getCrewId())
                .orElseThrow(() -> new IllegalArgumentException("Crew not found: " + dto.getCrewId()));

        Segment segment = null;
        if (dto.getSegmentId() != null) {
            segment = segmentRepository.findById(dto.getSegmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Segment not found: " + dto.getSegmentId()));
        }

        AssignmentStatus status = dto.getStatus() != null ? dto.getStatus() : AssignmentStatus.SCHEDULED;

        CrewAssignment assignment = CrewAssignment.builder()
                .crew(crew)
                .segment(segment)
                .nodeId(dto.getNodeId())
                .nodeType(dto.getNodeType())
                .nodeName(dto.getNodeName())
                .assignedDate(dto.getAssignedDate())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(status)
                .notes(dto.getNotes())
                .build();

        CrewAssignment saved = assignmentRepository.save(assignment);
        return assignmentMapper.toDto(saved);
    }

    /**
     * Actualiza una asignación existente
     */
    @Transactional
    public CrewAssignmentDTO updateAssignment(Long id, CrewAssignmentDTO dto) {
        dto.setId(id);
        ValidationResponseDTO validation = validateAssignment(dto);
        if (!validation.isValid()) {
            throw new IllegalArgumentException(String.join(", ", validation.getErrors()));
        }

        CrewAssignment existing = assignmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found with ID: " + id));

        Crew crew = crewRepository.findById(dto.getCrewId())
                .orElseThrow(() -> new IllegalArgumentException("Crew not found: " + dto.getCrewId()));

        Segment segment = null;
        if (dto.getSegmentId() != null) {
            segment = segmentRepository.findById(dto.getSegmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Segment not found: " + dto.getSegmentId()));
        }

        existing.setCrew(crew);
        existing.setSegment(segment);
        if (dto.getNodeId() != null) existing.setNodeId(dto.getNodeId());
        if (dto.getNodeType() != null) existing.setNodeType(dto.getNodeType());
        if (dto.getNodeName() != null) existing.setNodeName(dto.getNodeName());
        existing.setAssignedDate(dto.getAssignedDate());
        existing.setStartTime(dto.getStartTime());
        existing.setEndTime(dto.getEndTime());
        if (dto.getStatus() != null) {
            existing.setStatus(dto.getStatus());
        }
        existing.setNotes(dto.getNotes());

        CrewAssignment updated = assignmentRepository.save(existing);
        return assignmentMapper.toDto(updated);
    }

    /**
     * Elimina una asignación por ID
     */
    @Transactional
    public void deleteAssignment(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new IllegalArgumentException("Assignment not found with ID: " + id);
        }
        assignmentRepository.deleteById(id);
        log.info("Deleted assignment {}", id);
    }

    /**
     * Obtiene una asignación por ID
     */
    @Transactional(readOnly = true)
    public CrewAssignmentDTO getAssignmentById(Long id) {
        return assignmentRepository.findById(id)
                .map(assignmentMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("Assignment not found with ID: " + id));
    }

    /**
     * Obtiene asignaciones en un rango de fechas
     */
    @Transactional(readOnly = true)
    public List<CrewAssignmentDTO> getAssignmentsByDateRange(LocalDate start, LocalDate end) {
        List<CrewAssignment> list = assignmentRepository.findByAssignedDateBetween(start, end);
        return assignmentMapper.toDtoList(list);
    }

    /**
     * Obtiene cronograma de una cuadrilla
     */
    @Transactional(readOnly = true)
    public List<CrewAssignmentDTO> getCrewSchedule(Long crewId, LocalDate start, LocalDate end) {
        List<CrewAssignment> list = assignmentRepository.findByCrewIdAndAssignedDateBetween(crewId, start, end);
        return assignmentMapper.toDtoList(list);
    }

    /**
     * Obtiene cronograma de un proyecto
     */
    @Transactional(readOnly = true)
    public List<CrewAssignmentDTO> getProjectSchedule(Long projectId, LocalDate start, LocalDate end) {
        List<CrewAssignment> list = assignmentRepository.findByProjectIdAndAssignedDateBetween(projectId, start, end);
        return assignmentMapper.toDtoList(list);
    }

    /**
     * Obtiene asignaciones de un segmento
     */
    @Transactional(readOnly = true)
    public List<CrewAssignmentDTO> getSegmentAssignments(Long segmentId) {
        List<CrewAssignment> list = assignmentRepository.findBySegmentId(segmentId);
        return assignmentMapper.toDtoList(list);
    }
}

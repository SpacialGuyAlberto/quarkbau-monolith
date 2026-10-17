package com.quarkbau.monolith.schedule.controller;

import com.quarkbau.monolith.schedule.dto.CrewAssignmentDTO;
import com.quarkbau.monolith.schedule.dto.ValidationResponseDTO;
import com.quarkbau.monolith.schedule.service.ScheduleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/schedule")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    /**
     * Crear asignación de cuadrilla
     * Compatible con POST /api/schedule/assignments y alias /api/schedule/assign
     */
    @PostMapping({"/assignments", "/assign"})
    public ResponseEntity<?> createAssignment(@Valid @RequestBody CrewAssignmentDTO dto) {
        try {
            CrewAssignmentDTO created = scheduleService.createAssignment(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            log.warn("Validation/Creation failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Obtener asignaciones en rango de fechas (calendario semanal o mensual)
     * Compatible con GET /api/schedule/assignments?start=...&end=... y alias /api/schedule/week
     */
    @GetMapping({"/assignments", "/week"})
    public ResponseEntity<List<CrewAssignmentDTO>> getAssignments(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<CrewAssignmentDTO> assignments = scheduleService.getAssignmentsByDateRange(start, end);
        return ResponseEntity.ok(assignments);
    }

    /**
     * Obtener calendario de una cuadrilla en rango de fechas
     */
    @GetMapping({"/assignments/crew/{crewId}", "/crew/{crewId}"})
    public ResponseEntity<List<CrewAssignmentDTO>> getCrewSchedule(
            @PathVariable Long crewId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<CrewAssignmentDTO> schedule = scheduleService.getCrewSchedule(crewId, start, end);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Obtener calendario de un proyecto en rango de fechas
     */
    @GetMapping({"/assignments/project/{projectId}", "/project/{projectId}"})
    public ResponseEntity<List<CrewAssignmentDTO>> getProjectSchedule(
            @PathVariable Long projectId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        List<CrewAssignmentDTO> schedule = scheduleService.getProjectSchedule(projectId, start, end);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Obtener asignación por ID
     */
    @GetMapping("/assignments/{assignmentId}")
    public ResponseEntity<?> getAssignmentById(@PathVariable Long assignmentId) {
        try {
            CrewAssignmentDTO assignment = scheduleService.getAssignmentById(assignmentId);
            return ResponseEntity.ok(assignment);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Actualizar una asignación existente
     */
    @PutMapping({"/assignments/{assignmentId}", "/{assignmentId}"})
    public ResponseEntity<?> updateAssignment(
            @PathVariable Long assignmentId,
            @Valid @RequestBody CrewAssignmentDTO dto) {
        try {
            CrewAssignmentDTO updated = scheduleService.updateAssignment(assignmentId, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Eliminar una asignación
     */
    @DeleteMapping({"/assignments/{assignmentId}", "/{assignmentId}"})
    public ResponseEntity<?> deleteAssignment(@PathVariable Long assignmentId) {
        try {
            scheduleService.deleteAssignment(assignmentId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Validar una asignación sin guardarla
     * Compatible con POST /api/schedule/assignments/validate y alias /api/schedule/validate
     */
    @PostMapping({"/assignments/validate", "/validate"})
    public ResponseEntity<ValidationResponseDTO> validateAssignment(@RequestBody CrewAssignmentDTO dto) {
        ValidationResponseDTO response = scheduleService.validateAssignment(dto);
        return ResponseEntity.ok(response);
    }
}

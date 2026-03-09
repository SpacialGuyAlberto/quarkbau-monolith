package com.quarkbau.monolith.workflow.controller;

import com.quarkbau.monolith.workflow.model.WorkflowTask;
import com.quarkbau.monolith.workflow.repository.WorkflowTaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workflows")
@RequiredArgsConstructor
public class WorkflowController {

    private final WorkflowTaskRepository repository;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROJECT_MANAGER', 'SITE_MANAGER', 'FIELD_WORKER', 'SUBCONTRACTOR')")
    public List<WorkflowTask> getAllWorkflows() {
        List<WorkflowTask> tasks = repository.findAll();
        if (tasks.isEmpty()) {
            WorkflowTask t1 = new WorkflowTask();
            t1.setProjectName("Berlin Fiber Deployment");
            t1.setCurrentStep("Excavation");
            t1.setAssignedTo("Team Alpha");
            t1.setStatus("in-progress");
            t1.setDueDate(LocalDate.now().plusDays(5));
            repository.save(t1);

            WorkflowTask t2 = new WorkflowTask();
            t2.setProjectName("Munich Network Expansion");
            t2.setCurrentStep("Planning");
            t2.setAssignedTo("Team Beta");
            t2.setStatus("pending");
            t2.setDueDate(LocalDate.now().plusDays(10));
            repository.save(t2);

            return repository.findAll();
        }
        return tasks;
    }
}

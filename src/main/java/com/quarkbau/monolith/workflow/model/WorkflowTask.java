package com.quarkbau.monolith.workflow.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "workflow_tasks")
public class WorkflowTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;
    private String currentStep;
    private String assignedTo;
    private String status;
    private LocalDate dueDate;
}

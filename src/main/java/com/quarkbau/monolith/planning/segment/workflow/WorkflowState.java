package com.quarkbau.monolith.planning.segment.workflow;

public enum WorkflowState {
    PLAN, // Legacy value in DB
    PLANNED,
    SCHEDULED,
    IN_PROGRESS,
    BLOCKED,
    HOLD,
    CANCELLED,
    COMPLETED
}

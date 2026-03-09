package com.quarkbau.monolith.workflow.repository;

import com.quarkbau.monolith.workflow.model.WorkflowTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkflowTaskRepository extends JpaRepository<WorkflowTask, Long> {
}

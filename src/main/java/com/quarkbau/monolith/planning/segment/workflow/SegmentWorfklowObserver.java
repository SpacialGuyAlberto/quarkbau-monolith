package com.quarkbau.monolith.planning.segment.workflow;

import com.quarkbau.monolith.planning.segment.workflow.ConstructionPhaseState;
import com.quarkbau.monolith.planning.segment.core.Segment;
import com.quarkbau.monolith.planning.segment.workflow.WorkflowState;
import com.quarkbau.monolith.planning.segment.core.SegmentRepository;
import com.quarkbau.monolith.planning.segment.workflow.ConstructionPhaseFactory;
import com.quarkbau.monolith.planning.segment.core.SegmentService;
import org.springframework.context.event.EventListener;

import org.springframework.stereotype.Component;

@Component
public class SegmentWorfklowObserver {
    private final ConstructionPhaseFactory factory;
    private final SegmentRepository repository;

    public SegmentWorfklowObserver(ConstructionPhaseFactory factory, SegmentRepository repository) {
        this.factory = factory;
        this.repository = repository;
    }

    @EventListener
    public void onWorkflowStateChanged(WorkflowStateChangedEvent event) {
        Segment segment = event.getSegment();

        if (segment.getCurrentState() == WorkflowState.COMPLETED) {
            ConstructionPhaseState currentState = factory.getState(segment.getWorkType());
            currentState.advancePhase(segment);
            repository.save(segment);
        }
    }


}

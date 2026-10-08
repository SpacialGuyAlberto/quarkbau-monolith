package com.quarkbau.monolith.planning.event;

import com.quarkbau.monolith.planning.model.ConstructionPhaseState;
import com.quarkbau.monolith.planning.model.Segment;
import com.quarkbau.monolith.planning.model.WorkflowState;
import com.quarkbau.monolith.planning.repository.SegmentRepository;
import com.quarkbau.monolith.planning.service.ConstructionPhaseFactory;
import com.quarkbau.monolith.planning.service.SegmentService;
import org.springframework.context.event.EventListener;

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

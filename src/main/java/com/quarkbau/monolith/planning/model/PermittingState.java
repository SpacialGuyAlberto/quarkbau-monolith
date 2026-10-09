package com.quarkbau.monolith.planning.model;

import org.springframework.stereotype.Component;

@Component
public class PermittingState implements ConstructionPhaseState {

    @Override
    public void advancePhase(Segment segment) {
        segment.setWorkType(WorkType.EXCAVATION);
        segment.setCurrentState(WorkflowState.PLANNED);
    }

    @Override
    public WorkType getWorkType() {
        return WorkType.PERMITTING;
    }
}

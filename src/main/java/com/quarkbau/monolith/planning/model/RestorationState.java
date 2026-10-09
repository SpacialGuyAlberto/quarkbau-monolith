package com.quarkbau.monolith.planning.model;

import org.springframework.stereotype.Component;

@Component
public class RestorationState implements ConstructionPhaseState {

    @Override
    public void advancePhase(Segment segment) {
        segment.setWorkType(WorkType.FIBER_BLOWING);
        segment.setCurrentState(WorkflowState.PLANNED);
    }

    @Override
    public WorkType getWorkType() {
        return WorkType.RESTORATION;
    }
}

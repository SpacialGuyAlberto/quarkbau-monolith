package com.quarkbau.monolith.planning.model;

import org.springframework.stereotype.Component;

@Component
public class FiberBlowingState implements ConstructionPhaseState {

    @Override
    public void advancePhase(Segment segment) {
        segment.setWorkType(WorkType.SPLICING);
        segment.setCurrentState(WorkflowState.PLANNED);
    }

    @Override
    public WorkType getWorkType() {
        return WorkType.FIBER_BLOWING;
    }
}

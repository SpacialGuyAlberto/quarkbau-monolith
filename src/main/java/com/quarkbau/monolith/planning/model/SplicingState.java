package com.quarkbau.monolith.planning.model;

import org.springframework.stereotype.Component;

@Component
public class SplicingState implements ConstructionPhaseState {

    @Override
    public void advancePhase(Segment segment) {
        segment.setWorkType(WorkType.ASPHALT);
        segment.setCurrentState(WorkflowState.PLANNED);
    }

    @Override
    public WorkType getWorkType() {
        return WorkType.SPLICING;
    }
}

package com.quarkbau.monolith.planning.model;

import org.springframework.stereotype.Component;

@Component
public class QaState implements ConstructionPhaseState {

    @Override
    public void advancePhase(Segment segment) {
        segment.setWorkType(WorkType.DOCUMENTATION);
        segment.setCurrentState(WorkflowState.PLANNED);
    }

    @Override
    public WorkType getWorkType() {
        return WorkType.QA;
    }
}

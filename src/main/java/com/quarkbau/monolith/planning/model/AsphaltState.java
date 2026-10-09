package com.quarkbau.monolith.planning.model;

import org.springframework.stereotype.Component;

@Component
public class AsphaltState implements ConstructionPhaseState {

    @Override
    public void advancePhase(Segment segment) {
        segment.setWorkType(WorkType.QA);
        segment.setCurrentState(WorkflowState.PLANNED);
    }

    @Override
    public WorkType getWorkType() {
        return WorkType.ASPHALT;
    }
}

package com.quarkbau.monolith.planning.model;

import org.springframework.stereotype.Component;

@Component
public class ExcavationState implements ConstructionPhaseState {

    @Override
    public void advancePhase(Segment segment) {
        segment.setWorkType(WorkType.DUCT_INSTALLATION);
        segment.setCurrentState(WorkflowState.PLANNED);
    }

    @Override
    public WorkType getWorkType() {
        return WorkType.EXCAVATION;
    }
}

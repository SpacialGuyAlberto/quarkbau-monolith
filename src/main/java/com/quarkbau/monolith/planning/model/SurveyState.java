package com.quarkbau.monolith.planning.model;

import org.springframework.stereotype.Component;

@Component
public class SurveyState implements ConstructionPhaseState {

    @Override
    public void advancePhase(Segment segment) {
        segment.setWorkType(WorkType.EXCAVATION);
        segment.setCurrentState(WorkflowState.PLANNED);
    }

    @Override
    public WorkType getWorkType() {
        return WorkType.EXCAVATION;
    }
}

package com.quarkbau.monolith.planning.model;

import org.springframework.stereotype.Component;

@Component
public class DuctInstallationState implements ConstructionPhaseState {

    @Override
    public void advancePhase(Segment segment) {
        segment.setWorkType(WorkType.BACKFILL);
        segment.setCurrentState(WorkflowState.PLANNED);
    }

    @Override
    public WorkType getWorkType() {
        return WorkType.DUCT_INSTALLATION;
    }
}

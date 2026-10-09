package com.quarkbau.monolith.planning.segment.workflow;

import com.quarkbau.monolith.planning.segment.core.Segment;
import org.springframework.stereotype.Component;

@Component
public class TrenchingState implements ConstructionPhaseState {

    @Override
    public void advancePhase(Segment segment) {
        segment.setWorkType(WorkType.DRILLING);
        segment.setCurrentState(WorkflowState.PLANNED);
    }

    @Override
    public WorkType getWorkType() {
        return WorkType.TRENCHING;
    }
}

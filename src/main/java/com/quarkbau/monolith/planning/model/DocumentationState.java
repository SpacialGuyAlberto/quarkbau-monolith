package com.quarkbau.monolith.planning.model;

import org.springframework.stereotype.Component;

@Component
public class DocumentationState implements ConstructionPhaseState {

    @Override
    public void advancePhase(Segment segment) {
        // This is the final state. The segment is fully complete.
        // It stays as COMPLETED, no WorkType change.
    }

    @Override
    public WorkType getWorkType() {
        return WorkType.DOCUMENTATION;
    }
}

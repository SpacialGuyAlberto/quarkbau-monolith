package com.quarkbau.monolith.planning.segment.workflow;

import com.quarkbau.monolith.planning.segment.core.Segment;


public interface ConstructionPhaseState {
    default void onPlanned(Segment segment) {
    }

    default void onStarted(Segment segment) {
    }

    default void onCompleted(Segment segment) {
    }

    void advancePhase(Segment segment);

    WorkType getWorkType();
}

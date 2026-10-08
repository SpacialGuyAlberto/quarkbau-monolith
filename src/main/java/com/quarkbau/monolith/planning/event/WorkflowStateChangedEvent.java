package com.quarkbau.monolith.planning.event;

import com.quarkbau.monolith.planning.model.Segment;
import org.springframework.context.ApplicationEvent;

public class WorkflowStateChangedEvent extends ApplicationEvent {
    private final Segment segment;
    public WorkflowStateChangedEvent(Object source, Segment segment) {
        super(source);
        this.segment = segment;
    }
    public Segment getSegment() {
        return segment;
    }

}

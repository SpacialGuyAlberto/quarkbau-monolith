package com.quarkbau.monolith.planning.exception;

public class CrewBusyException extends RuntimeException {
    private final Long crewId;

    public CrewBusyException(String message, Long crewId) {
        super(message);
        this.crewId = crewId;
    }

    public Long getCrewId() {
        return crewId;
    }
}

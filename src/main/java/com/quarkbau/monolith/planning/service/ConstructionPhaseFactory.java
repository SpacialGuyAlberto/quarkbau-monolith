package com.quarkbau.monolith.planning.service;


import com.quarkbau.monolith.planning.model.ConstructionPhaseState;
import com.quarkbau.monolith.planning.model.WorkType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ConstructionPhaseFactory {
    private final Map<WorkType, ConstructionPhaseState> states;


    public ConstructionPhaseFactory(List<ConstructionPhaseState> statesList) {
       this.states = statesList.stream()
               .collect(Collectors.toMap(ConstructionPhaseState::getWorkType, Function.identity()));
    }

    public ConstructionPhaseState getState(WorkType workType) {
        ConstructionPhaseState state = states.get(workType);
        if (state == null) {
            throw new IllegalArgumentException("Work type not found: " + workType);
        }
        return state;
    }


}

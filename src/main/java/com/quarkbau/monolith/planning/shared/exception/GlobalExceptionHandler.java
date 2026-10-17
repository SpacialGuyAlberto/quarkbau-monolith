package com.quarkbau.monolith.planning.shared.exception;

import com.quarkbau.monolith.planning.workforce.team.CrewBusyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CrewBusyException.class)
    public ResponseEntity<Map<String, Object>> handleCrewBusyException(CrewBusyException ex) {
        Map<String, Object> body = new HashMap<>();
        body.put("error", "CREW_BUSY");
        body.put("message", ex.getMessage());
        body.put("crewId", ex.getCrewId());
        
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }
}

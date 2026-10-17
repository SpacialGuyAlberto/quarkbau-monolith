package com.quarkbau.monolith.shared.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Fundamental para que el Dashboard en Angular/React o Unity no sea bloqueado por CORS
public class NotificationController {

    private final SseNotificationService sseNotificationService;

    // Endpoint al que se conectará Unity con UnityWebRequest o el navegador Web
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter stream(@RequestParam("userId") String userId) {
        // En una app real, el userId se saca del token JWT en lugar de QueryParam
        return sseNotificationService.subscribe(userId);
    }
}

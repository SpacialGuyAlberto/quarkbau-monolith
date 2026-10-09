package com.quarkbau.monolith.shared.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Service
public class SseNotificationService {

    // Almacena los clientes SSE conectados: recipientId -> Lista de Emitters
    private final Map<String, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(String recipientId) {
        // Configuramos el timeout a un valor muy alto para mantener la conexión viva en Gafas y Web
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        emitters.computeIfAbsent(recipientId, k -> new CopyOnWriteArrayList<>()).add(emitter);

        emitter.onCompletion(() -> removeEmitter(recipientId, emitter));
        emitter.onTimeout(() -> removeEmitter(recipientId, emitter));
        emitter.onError((e) -> removeEmitter(recipientId, emitter));

        try {
            // Mandamos un evento inicial vacío para forzar que los headers HTTP se envíen
            emitter.send(SseEmitter.event().name("CONNECT").data("Conectado exitosamente al canal: " + recipientId));
            log.info("Nuevo cliente conectado al canal SSE de: {}", recipientId);
        } catch (IOException e) {
            removeEmitter(recipientId, emitter);
        }

        return emitter;
    }

    private void removeEmitter(String recipientId, SseEmitter emitter) {
        List<SseEmitter> userEmitters = emitters.get(recipientId);
        if (userEmitters != null) {
            userEmitters.remove(emitter);
            if (userEmitters.isEmpty()) {
                emitters.remove(recipientId);
            }
        }
    }

    public void dispatch(NotificationMessage message) {
        String recipientId = message.recipientId();
        List<SseEmitter> userEmitters = emitters.get(recipientId);

        if (userEmitters != null && !userEmitters.isEmpty()) {
            for (SseEmitter emitter : userEmitters) {
                try {
                    // El formato de SSE se emite aquí con el payload (el record NotificationMessage como JSON)
                    emitter.send(SseEmitter.event()
                            .name("NOTIFICATION")
                            .data(message));
                    log.info("SSE emitido a {}: {}", recipientId, message.title());
                } catch (IOException e) {
                    log.error("Error enviando SSE a {}", recipientId, e);
                    emitter.complete();
                    removeEmitter(recipientId, emitter);
                }
            }
        } else {
            log.debug("No hay clientes conectados escuchando a {}", recipientId);
        }
    }
}

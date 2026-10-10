package com.quarkbau.monolith.shared.notification.channels;

import com.quarkbau.monolith.shared.notification.NotificationChannel;
import com.quarkbau.monolith.shared.notification.NotificationMessage;
import com.quarkbau.monolith.shared.notification.NotificationSender;
import com.quarkbau.monolith.shared.notification.SseNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PushGlassesNotificationSender implements NotificationSender {

    private final SseNotificationService sseNotificationService;

    @Override
    public NotificationChannel supportsChannel() {
        return NotificationChannel.PUSH_GLASSES;
    }

    @Override
    public void send(NotificationMessage message) {
        log.info("[GLASSES PUSH] Evaluando conexión en tiempo real para las gafas de {}...", message.recipientId());
        // Despachar a través del servicio SSE que empujará a la app de Unity
        sseNotificationService.dispatch(message);
    }
}

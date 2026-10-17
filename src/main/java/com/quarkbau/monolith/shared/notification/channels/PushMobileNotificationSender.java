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
public class PushMobileNotificationSender implements NotificationSender {

    private final SseNotificationService sseNotificationService;

    @Override
    public NotificationChannel supportsChannel() {
        return NotificationChannel.PUSH_MOBILE;
    }

    @Override
    public void send(NotificationMessage message) {
        log.info("[MOBILE PUSH] Evaluando conexión en tiempo real para la App de {}...", message.recipientId());
        // Despachar a través del servicio SSE para enviarlo instantáneamente a la app móvil (Flutter)
        sseNotificationService.dispatch(message);
    }
}

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
public class WebDashboardNotificationSender implements NotificationSender {

    private final SseNotificationService sseNotificationService;

    @Override
    public NotificationChannel supportsChannel() {
        return NotificationChannel.WEB_DASHBOARD;
    }

    @Override
    public void send(NotificationMessage message) {
        log.info("[WEB DASHBOARD] Actualizando Backoffice ({})", message.recipientId());
        // Despachar a través del servicio SSE que empujará al navegador web (React/Angular)
        sseNotificationService.dispatch(message);
    }
}

package com.quarkbau.monolith.shared.notification;

public interface NotificationSender {
    NotificationChannel supportsChannel();
    void send(NotificationMessage message);
}

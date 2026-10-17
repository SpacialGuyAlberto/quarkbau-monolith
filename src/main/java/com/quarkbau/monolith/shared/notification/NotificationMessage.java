package com.quarkbau.monolith.shared.notification;

public record NotificationMessage(
    String recipientId,
    String title,
    String body,
    NotificationChannel channel,
    String entityId
) {}

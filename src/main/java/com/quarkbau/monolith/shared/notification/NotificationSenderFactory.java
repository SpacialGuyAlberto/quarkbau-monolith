package com.quarkbau.monolith.shared.notification;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class NotificationSenderFactory {

    private final Map<NotificationChannel, NotificationSender> senders;

    public NotificationSenderFactory(List<NotificationSender> senderList) {
        this.senders = senderList.stream()
                .collect(Collectors.toMap(NotificationSender::supportsChannel, Function.identity()));
    }

    public NotificationSender getSender(NotificationChannel channel) {
        NotificationSender sender = senders.get(channel);
        if (sender == null) {
            throw new IllegalArgumentException("Canal no soportado: " + channel);
        }
        return sender;
    }
}

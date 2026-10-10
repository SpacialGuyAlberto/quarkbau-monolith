package com.quarkbau.monolith.planning.segment.workflow;

import com.quarkbau.monolith.planning.segment.core.Segment;
import com.quarkbau.monolith.shared.notification.NotificationChannel;
import com.quarkbau.monolith.shared.notification.NotificationMessage;
import com.quarkbau.monolith.shared.notification.NotificationSenderFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkflowNotificationListener {

    private final NotificationSenderFactory senderFactory;

    @Async("notificationExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleWorkflowStateChanged(WorkflowStateChangedEvent event) {
        Segment segment = event.getSegment();

        // Enviar notificaciones para TODOS los estados sin excepción
        String title = "Segmento Actualizado: " + segment.getCurrentState().name();
        String body = "El segmento " + segment.getStreetName() + " (Fase " + segment.getWorkType() + ") ha cambiado a " + segment.getCurrentState().name() + ".";
        
        // Mensajes específicos
        if (segment.getCurrentState() == WorkflowState.COMPLETED) {
            title = "Fase Completada: " + segment.getWorkType();
            body = "El segmento " + segment.getStreetName() + " ha completado la fase de " + segment.getWorkType() + ".";
        } else if (segment.getCurrentState() == WorkflowState.BLOCKED) {
            title = "¡Alerta! Segmento Bloqueado";
            body = "El trabajo en " + segment.getStreetName() + " se ha bloqueado de emergencia.";
        } else if (segment.getCurrentState() == WorkflowState.PLAN) {
            title = "Nuevo Segmento Planificado";
            body = "Se ha planificado el segmento " + segment.getStreetName() + " para " + segment.getWorkType() + ".";
        }

        // Para testing: enviaremos a '1' además de la crew asignada, así el dev app lo recibe.
        String targetUser = segment.getAssignedCrew() != null 
                ? String.valueOf(segment.getAssignedCrew().getId()) 
                : "1";

        // Enviar notificación a la App Móvil para la crew asignada
        NotificationMessage mobileMessage = new NotificationMessage(targetUser, title, body, NotificationChannel.PUSH_MOBILE, segment.getId().toString());
        senderFactory.getSender(NotificationChannel.PUSH_MOBILE).send(mobileMessage);

        // FOR DEMO: Siempre enviarle a userId 1 para que el tester lo vea en la app
        if (!targetUser.equals("1")) {
            NotificationMessage demoMessage = new NotificationMessage("1", title, body, NotificationChannel.PUSH_MOBILE, segment.getId().toString());
            senderFactory.getSender(NotificationChannel.PUSH_MOBILE).send(demoMessage);
        }

        // Enviar notificación al Dashboard Web en tiempo real (para PMs y Backoffice)
        NotificationMessage webMessage = new NotificationMessage("BACKOFFICE", title, body, NotificationChannel.WEB_DASHBOARD, segment.getId().toString());
        senderFactory.getSender(NotificationChannel.WEB_DASHBOARD).send(webMessage);

        // Si el WorkType lo requiere, enviar al HUD de las gafas AR del capataz en terreno
        NotificationMessage glassesMessage = new NotificationMessage(targetUser, title, body, NotificationChannel.PUSH_GLASSES, segment.getId().toString());
        senderFactory.getSender(NotificationChannel.PUSH_GLASSES).send(glassesMessage);

        // FOR DEMO: Siempre enviarle a userId 1 para que la app de Unity lo reciba (ya que foremanId = "1" por defecto)
        if (!targetUser.equals("1")) {
            NotificationMessage demoGlassesMessage = new NotificationMessage("1", title, body, NotificationChannel.PUSH_GLASSES, segment.getId().toString());
            senderFactory.getSender(NotificationChannel.PUSH_GLASSES).send(demoGlassesMessage);
        }
    }
}

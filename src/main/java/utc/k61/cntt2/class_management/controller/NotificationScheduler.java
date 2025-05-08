package utc.k61.cntt2.class_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import utc.k61.cntt2.class_management.domain.Notification;
import utc.k61.cntt2.class_management.repository.NotificationRepository;

@Component
public class NotificationScheduler {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private NotificationRepository notificationRepository;

    @Scheduled(fixedRate = 60000)
    public void sendScheduledNotification() {
        Notification notification = notificationRepository.findByIsReadIsFalse();
        messagingTemplate.convertAndSend("/topic/notification/" + notification.getToUserNumber(), notification);
    }
}

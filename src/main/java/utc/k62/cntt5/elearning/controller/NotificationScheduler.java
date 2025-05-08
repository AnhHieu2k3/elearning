package utc.k62.cntt5.elearning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import utc.k62.cntt5.elearning.domain.Notification;
import utc.k62.cntt5.elearning.repository.NotificationRepository;

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

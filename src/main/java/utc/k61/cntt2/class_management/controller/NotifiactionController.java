package utc.k61.cntt2.class_management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import utc.k61.cntt2.class_management.dto.NotificationDto;
import utc.k61.cntt2.class_management.repository.NotificationRepository;
import utc.k61.cntt2.class_management.service.NotificationService;

import java.util.Map;

@RestController
@RequestMapping("/api/notification")
public class NotifiactionController {
    private final NotificationService notificationService;
    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotifiactionController(
            NotificationService notificationService,
            SimpMessagingTemplate messagingTemplate,
            NotificationRepository notificationRepository) {
        this.notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getClassDetail(@RequestParam String userId) {
        return ResponseEntity.ok(notificationService.getNotification(userId));
    }

    @GetMapping("/all-notifications")
    public ResponseEntity<Page<NotificationDto>> getNotifications(
            @RequestParam String userId,
            @RequestParam int page,
            @RequestParam int size
    ) {
        Page<NotificationDto> result = notificationService.getAllNotifications(userId, page, size);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/mark-read")
    public ResponseEntity<Void> markAllAsRead(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        notificationRepository.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }
}

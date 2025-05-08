package utc.k61.cntt2.class_management.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import utc.k61.cntt2.class_management.domain.Notification;
import utc.k61.cntt2.class_management.dto.NotificationDto;
import utc.k61.cntt2.class_management.repository.NotificationRepository;

@Log4j2
@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(NotificationDto notiDto) {
        Notification notification = new Notification();
        notification.setContent(notiDto.getContent());
        notification.setType(notiDto.getType());
        notification.setFromUserNumber(notiDto.getFromUserNumber());
        notification.setToUserNumber(notiDto.getToUserNumber());
        return notificationRepository.save(notification);
    }

    public NotificationDto getNotification(String userId) {
        Notification notification = notificationRepository.findByToUserNumber(userId);
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setContent(notification.getContent());
        notificationDto.setType(notification.getType());
        notificationDto.setFromUserNumber(notification.getFromUserNumber());
        notificationDto.setToUserNumber(notification.getToUserNumber());
        return notificationDto;
    }

    public Page<NotificationDto> getAllNotifications(String userId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<Notification> notifications = notificationRepository.findByToUserNumberOrderByCreatedDateDesc(userId, pageRequest);
        Page<NotificationDto> notificationDtos = notifications.map(notification -> new NotificationDto());
        return notificationDtos;
    }
}

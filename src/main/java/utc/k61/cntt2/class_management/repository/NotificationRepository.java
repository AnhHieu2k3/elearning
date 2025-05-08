package utc.k61.cntt2.class_management.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import utc.k61.cntt2.class_management.domain.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByToUserNumber(String userId);

    Page<Notification> findByToUserNumberOrderByCreatedDateDesc(String toUserNumber, Pageable pageable);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.toUserNumber = :userId AND n.isRead = false")
    void markAllAsRead(String userId);

    Notification findByIsReadIsFalse();
}

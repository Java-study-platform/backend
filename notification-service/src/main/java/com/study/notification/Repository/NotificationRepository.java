package com.study.notification.Repository;

import com.study.notification.Entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    @Query(value = "SELECT COUNT(*) FROM notifications WHERE is_read = false AND user_email = :userEmail", nativeQuery = true)
    Integer countByIsReadFalse(String userEmail);

    @Query(value = "SELECT * FROM notifications WHERE is_read = false AND user_email = :userEmail", nativeQuery = true)
    List<Notification> findAllUnreadNotificationsByUserLogin(String userEmail);

    Page<Notification> findAllByUserEmailOrderByIsReadAsc(Pageable pageable, String userEmail);

    Optional<Notification> findByUserEmailAndId(String userEmail, UUID id);
}

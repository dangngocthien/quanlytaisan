package com.nhom18.quanlytaisan.service;

import com.nhom18.quanlytaisan.entity.Notification;
import com.nhom18.quanlytaisan.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(String title, String message, String relatedPath) {
        Notification notification = new Notification(title, message, relatedPath);
        notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();
    }

    public List<Notification> getRecentNotifications() {
        return notificationRepository.findTop50ByOrderByCreatedAtDesc();
    }

    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    public void markAllAsRead() {
        List<Notification> unreadList = notificationRepository.findByIsReadFalseOrderByCreatedAtDesc();
        unreadList.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unreadList);
    }
}

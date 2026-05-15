package at.jku.studentgraphdb.service;

import at.jku.studentgraphdb.models.Notification;
import at.jku.studentgraphdb.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> findAllNotifications() {
        // order by deletion time
        return notificationRepository.findAllByOrderByDeletedAtDesc();
    }
}

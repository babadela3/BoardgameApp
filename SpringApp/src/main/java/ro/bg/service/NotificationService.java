package ro.bg.service;

import ro.bg.model.Notification;
import ro.bg.model.User;

import java.util.List;

public interface NotificationService {

    void createNotification(Notification notification);

    List<Notification> getAllNotifications(User user);
}

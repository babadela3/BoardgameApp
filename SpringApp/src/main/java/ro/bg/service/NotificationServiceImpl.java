package ro.bg.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ro.bg.dao.NotificationDAO;
import ro.bg.model.Notification;
import ro.bg.model.User;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    NotificationDAO notificationDAO;

    @Override
    public void createNotification(Notification notification) {
        notificationDAO.saveAndFlush(notification);
    }

    @Override
    public List<Notification> getAllNotifications(User user) {
        return notificationDAO.getNotifications(user.getId());
    }
}

package ro.bg.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ro.bg.model.Notification;
import ro.bg.model.User;
import ro.bg.service.NotificationService;

import java.util.List;

@Controller
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @RequestMapping(value = "/createNotification", method = RequestMethod.POST)
    public String createNotification(@RequestBody Notification notification) {
        notificationService.createNotification(notification);
        return "";
    }

    @RequestMapping(value = "/allNotifications", method = RequestMethod.POST)
    public String getNotifications(@RequestBody User user) {
        List<Notification> notificationList = notificationService.getAllNotifications(user);
        for(Notification notification : notificationList){
            System.out.println(notification);
        }
        return "";
    }
}

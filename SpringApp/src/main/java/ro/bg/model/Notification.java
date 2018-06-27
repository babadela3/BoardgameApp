package ro.bg.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import ro.bg.model.constants.NotificationTypeEnum;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "pk_notification_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "fk_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "fk_friendship_request_id")
    private FriendshipRequest friendshipRequest;

    @ManyToOne
    @JoinColumn(name = "fk_event_id")
    private Event event;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type")
    private NotificationTypeEnum notificationTypeEnum;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    @Column(name = "data")
    private Date date;

    @Column(name = "message")
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public FriendshipRequest getFriendshipRequest() {
        return friendshipRequest;
    }

    public void setFriendshipRequest(FriendshipRequest friendshipRequest) {
        this.friendshipRequest = friendshipRequest;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public NotificationTypeEnum getNotificationTypeEnum() {
        return notificationTypeEnum;
    }

    public void setNotificationTypeEnum(NotificationTypeEnum notificationTypeEnum) {
        this.notificationTypeEnum = notificationTypeEnum;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

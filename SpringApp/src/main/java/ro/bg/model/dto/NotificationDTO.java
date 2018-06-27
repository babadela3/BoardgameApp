package ro.bg.model.dto;

public class NotificationDTO {

    private int id;

    private String notificationTypeEnum;

    private int userId;

    private int eventId;

    private String message;

    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotificationTypeEnum() {
        return notificationTypeEnum;
    }

    public void setNotificationTypeEnum(String notificationTypeEnum) {
        this.notificationTypeEnum = notificationTypeEnum;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

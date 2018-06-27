package ro.bg.model.constants;

public enum NotificationTypeEnum {
    INVITATION_EVENT("INVITATION_EVENT"),
    REQUEST_FRIENDSHIP("REQUEST_FRIENDSHIP"),
    REQUEST_FRIENDSHIP_ACCEPTED("REQUEST_FRIENDSHIP_ACCEPTED"),
    REQUEST_EVENT_ACCEPTED("REQUEST_EVENT_ACCEPTED"),
    REQUEST_EVENT_PARTICIPATION("REQUEST_EVENT_PARTICIPATION");

    private final String str;
    private NotificationTypeEnum(String s){
        str = s;
    }
    public String toString() {
        return str;
    }

}

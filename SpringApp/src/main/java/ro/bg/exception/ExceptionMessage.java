package ro.bg.exception;

public enum  ExceptionMessage {

    MISSING_PUB("BG-01", "Pub is not registred in the application"),
    EMAIL_NOT_EXISTING("BG-02", "The email is not registred"),
    MISSING_USER("BG-03", "User is not registred in the application"),
    USER_ALREADY_EXISTS("BG-04", "User is already registred in the application"),
    PUB_ALREADY_EXISTS("BG-05", "Pub is already registred in the application");

    private String code;
    private String message;


    ExceptionMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public String toString() {
        return "ExceptionMessage{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}

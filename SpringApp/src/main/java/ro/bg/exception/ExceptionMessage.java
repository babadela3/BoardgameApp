package ro.bg.exception;

public enum  ExceptionMessage {

    MISSING_PUB("BG-01", "The email or password is invalid."),
    EMAIL_NOT_EXISTING("BG-02", "The email is not registered"),
    MISSING_USER("BG-03", "User is not registered in the application"),
    USER_ALREADY_EXISTS("BG-04", "User is already registered in the application"),
    PUB_ALREADY_EXISTS("BG-05", "The email is already registered."),
    INVALID_TOKEN("BG-06","The token is invalid.");

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

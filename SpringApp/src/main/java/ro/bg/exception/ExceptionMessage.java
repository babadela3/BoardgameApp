package ro.bg.exception;

public enum  ExceptionMessage {

    MISSING_PUB("BG-01", "Pub is not registred in the application");

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

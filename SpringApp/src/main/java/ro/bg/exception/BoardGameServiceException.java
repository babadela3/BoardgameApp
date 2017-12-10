package ro.bg.exception;

public class BoardGameServiceException extends Exception{

    private String code;

    public BoardGameServiceException(ExceptionMessage exceptionMessage, Throwable cause) {
        super(exceptionMessage.getMessage(), cause);
        this.code = exceptionMessage.getCode();
    }

    public BoardGameServiceException(ExceptionMessage exceptionMessage) {
        super(exceptionMessage.getMessage());
        this.code = exceptionMessage.getCode();
    }

    public String getCode() {
        return code;
    }
}
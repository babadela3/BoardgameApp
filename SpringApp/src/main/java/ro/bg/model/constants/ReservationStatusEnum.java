package ro.bg.model.constants;

public enum ReservationStatusEnum {
    ACCEPTED("ACCEPTED"),
    REJECTED("REJECTED"),
    PEDDING("PEDDING"),
    NONE("NONE");

    private final String str;
    private ReservationStatusEnum(String s){
        str = s;
    }
    public String toString() {
        return str;
    }
}



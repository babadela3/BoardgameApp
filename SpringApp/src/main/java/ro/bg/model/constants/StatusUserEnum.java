package ro.bg.model.constants;

public enum StatusUserEnum {
    PARTICIPANT("PARTICIPANT"),
    INVITED("INVITED"),
    INTERESTED("INTERESTED"),
    WAITING("WAITING"),
    REJECTED("REJECTED");

    private final String str;
    private StatusUserEnum(String s){
        str = s;
    }
    public String toString() {
        return str;
    }
}

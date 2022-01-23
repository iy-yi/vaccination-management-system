package cmpe275.vms.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Status {
    CHECKED("Checked in"),
    CANCELED("Canceled"),
    BOOKED("Booked");

    private final String value;

    Status(String status) {this.value = status;}

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @JsonCreator
    public static Status fromText(String text) {
        for(Status r : Status.values()){
            if(r.getValue().equals(text)){
                return r;
            }
        }
        throw new IllegalArgumentException();
    }
}

package cmpe275.vms.models.enums;

import com.fasterxml.jackson.annotation.JsonCreator;


public enum Gender {
    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private final String value;

    Gender(String value) {
        this.value = value;
    }

}

package cmpe275.vms.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Data
@Embeddable
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Address {
    @Column(nullable = false)
    private String street;
    
    private String number;
    private String city;
    private String state;
    private String zip;

    public Address(String street, String number, String city, String state, String zip) {
        this.number = number;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zip = zip;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}

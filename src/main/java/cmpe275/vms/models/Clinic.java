package cmpe275.vms.models;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalTime;

@Entity
public class Clinic {
    @Id
    private String name;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @Column(nullable = false)
    private int numberOfPhysicians;

    @Column(nullable = false)
    private int open;

    @Column(nullable = false)
    private int close;

    public Clinic() {
    }

    public Clinic(String name) {
        this.name = name;
    }

    public Clinic(String name, Address address, int numberOfPhysicians, int open, int close) {
        this.name = name;
        this.address = address;
        this.numberOfPhysicians = numberOfPhysicians;
        this.open = open;
        this.close = close;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public int getClose() {
        return close;
    }

    public void setClose(int close) {
        this.close = close;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getNumberOfPhysicians() {
        return numberOfPhysicians;
    }

    public void setNumberOfPhysicians(int numberOfPhysicians) {
        this.numberOfPhysicians = numberOfPhysicians;
    }
}

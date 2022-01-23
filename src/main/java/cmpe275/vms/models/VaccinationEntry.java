package cmpe275.vms.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class VaccinationEntry implements Comparable<VaccinationEntry> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(targetEntity = Vaccination.class, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"diseases"})
    private Vaccination vaccination;

    @OneToOne(targetEntity = Appointment.class, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"user", "vaccines"})
    private Appointment appointment;

    private int shot;
    private LocalDateTime due;
    private boolean complete;

    public VaccinationEntry() {
    }

    public VaccinationEntry(Vaccination vaccination, int shot, boolean complete) {
        this.vaccination = vaccination;
        this.shot = shot;
        this.complete = complete;
    }

    public VaccinationEntry(Vaccination vaccination, Appointment appointment, int shot, LocalDateTime due, boolean complete) {
        this.vaccination = vaccination;
        this.appointment = appointment;
        this.shot = shot;
        this.due = due;
        this.complete = complete;
    }

    public int compareTo(VaccinationEntry vaccinationEntry) {
        LocalDateTime date1 = this.getDue();
        LocalDateTime date2 = vaccinationEntry.getDue();
        return date1.compareTo(date2);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Vaccination getVaccination() {
        return vaccination;
    }

    public void setVaccination(Vaccination vaccination) {
        this.vaccination = vaccination;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public int getShot() {
        return shot;
    }

    public void setShot(int shot) {
        this.shot = shot;
    }

    public LocalDateTime getDue() {
        return due;
    }

    public void setDue(LocalDateTime due) {
        this.due = due;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }
}

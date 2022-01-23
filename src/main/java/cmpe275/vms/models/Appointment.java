package cmpe275.vms.models;

import cmpe275.vms.models.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Entity
public class Appointment implements Comparable<Appointment>{

    @Id
    @GenericGenerator(name = "id_generator", strategy = "cmpe275.vms.utils.IdGenerator")
    @GeneratedValue(generator = "id_generator")
    private String id;

    @Column(nullable = false)
    private Status status = Status.BOOKED;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private LocalDateTime date;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @OneToOne(targetEntity = Clinic.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Clinic clinic;

    @ManyToMany(targetEntity = Vaccination.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"diseases"})
    private List<Vaccination> vaccines;

    @ManyToOne(targetEntity = User.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"appointments", "address", "password", "vaccines", "role"})
    private User user;

    public Appointment() {
    }

    public Appointment(String id, User user, Status status, LocalDateTime date, LocalDateTime createDate, Clinic clinic, List<Vaccination> vaccines) {
        this.id = id;
        this.user = user;
        this.status = status;
        this.date = date;
        this.createDate = createDate;
        this.clinic = clinic;
        this.vaccines = vaccines;
    }

    @Override
    public int compareTo(Appointment u) {
        return this.getDate().compareTo(u.getDate());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Clinic getClinic() {
        return clinic;
    }

    public void setClinic(Clinic clinic) {
        this.clinic = clinic;
    }

    public List<Vaccination> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<Vaccination> vaccines) {
        this.vaccines = vaccines;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}

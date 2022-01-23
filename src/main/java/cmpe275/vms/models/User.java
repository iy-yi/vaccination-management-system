package cmpe275.vms.models;

import cmpe275.vms.models.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import cmpe275.vms.models.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {
    @Id
    @GenericGenerator(name = "id_generator", strategy = "cmpe275.vms.utils.IdGenerator")
    @GeneratedValue(generator = "id_generator")
    private String mrn;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    private String middleName;

    @Column(nullable = false)
    private String dob;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @Column(nullable = false)
    private Gender gender;

    private boolean activated = false;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLES",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID") })
    private Set<Role> role;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

//    @ManyToMany(targetEntity = Vaccination.class)
//    private List<Vaccination> futureVaccines;
//
//    @ManyToMany(targetEntity = Vaccination.class)
//    private List<Vaccination> vaccineHistory;
    @OneToMany(targetEntity = VaccinationEntry.class)
    @JsonIgnoreProperties({"appointment", "vaccination"})
    private List<VaccinationEntry> vaccines;

    @OneToMany(targetEntity = Appointment.class)
    @JsonIgnoreProperties({"user", "vaccines", "clinic"})
    private List<Appointment> appointments;

    public User() {
    }

    public User(String email, String firstName, String lastName, String dob, Address address, Gender gender, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.address = address;
        this.gender = gender;
        this.password = password;
        this.role = new HashSet<>();
    }

    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public List<VaccinationEntry> getVaccines() {
        return vaccines;
    }

    public void setVaccines(List<VaccinationEntry> vaccines) {
        this.vaccines = vaccines;
    }

    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}

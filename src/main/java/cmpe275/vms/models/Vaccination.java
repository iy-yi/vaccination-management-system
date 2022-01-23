package cmpe275.vms.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(targetEntity = Disease.class, fetch = FetchType.LAZY)
    private List<Disease> diseases;

    @Column(nullable = false)
    private String manufacturer;

    @Column(nullable = false)
    private int numOfShots;

    private int shotInterval;

    @Column(nullable = false)
    private int duration;

    public Vaccination() {
    }



    public Vaccination(Long id, String name, List<Disease> diseases, String manufacturer, int numOfShots, int shotInterval, int duration) {
        this.id = id;
        this.name = name;
        this.diseases = diseases;
        this.manufacturer = manufacturer;
        this.numOfShots = numOfShots;
        this.shotInterval = shotInterval;
        this.duration = duration;
    }

    public Vaccination(Long id, String name, List<Disease> diseases, String manufacturer, int numOfShots, int duration) {
        this.id = id;
        this.name = name;
        this.diseases = diseases;
        this.manufacturer = manufacturer;
        this.numOfShots = numOfShots;
        this.duration = duration;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Disease> getDiseases() {
        return diseases;
    }

    public void setDiseases(List<Disease> diseases) {
        this.diseases = diseases;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getNumOfShots() {
        return numOfShots;
    }

    public void setNumOfShots(int numOfShots) {
        this.numOfShots = numOfShots;
    }

    public int getShotInterval() {
        return shotInterval;
    }

    public void setShotInterval(int shotInterval) {
        this.shotInterval = shotInterval;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}

package cmpe275.vms.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
public class Disease {
    @Id
    @GenericGenerator(name = "id_generator", strategy = "cmpe275.vms.utils.IdGenerator")
    @GeneratedValue(generator = "id_generator")
    private String id;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    public Disease(String name) {
        this.name = name;
    }

    public Disease(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Disease() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

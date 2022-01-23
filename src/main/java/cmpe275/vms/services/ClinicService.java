package cmpe275.vms.services;

import cmpe275.vms.models.Clinic;
import org.apache.tomcat.jni.Local;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ClinicService {
    Clinic create(Clinic clinic);
    Clinic update(Clinic clinic);
    void delete(String name);
    Clinic get(String name);
    List<Clinic> findAll();
    List<Clinic> findAllAvailable(LocalDateTime time, int hour);
}

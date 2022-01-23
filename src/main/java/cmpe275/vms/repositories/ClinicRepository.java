package cmpe275.vms.repositories;

import cmpe275.vms.models.Clinic;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, String> {
    @Query("select c from Clinic c left join Appointment a on a.clinic = c.name WHERE NOT a.status = 1 and a.date = ?1 and c.open <= ?2 and c.close > ?2\n" +
            " group by c.name having COUNT(c) < c.numberOfPhysicians")
    List<Clinic> getAvailableClinic(LocalDateTime date, int hour);

    @Query("select c from Clinic c where c.name NOT IN (select a.clinic from Appointment a where a.date =?1) and c.open <= ?2 and c.close > ?2")
    List<Clinic> getFreeClinic(LocalDateTime date, int hour);
}
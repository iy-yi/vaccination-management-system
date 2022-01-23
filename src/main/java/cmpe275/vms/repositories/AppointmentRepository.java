package cmpe275.vms.repositories;

import cmpe275.vms.models.Appointment;
import cmpe275.vms.models.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, String> {
    @Query("SELECT COUNT(a) FROM Appointment a WHERE NOT a.status = 1 and a.date=?1 and a.clinic=?2")
    long getAppointmentCount(LocalDateTime date, Clinic name);

    @Query("SELECT a from Appointment a where a.user.email = ?1 and a.date >= ?2 order by a.date asc")
    List<Appointment> getFutureAppointment(String email, LocalDateTime date);

    @Query("SELECT a from Appointment a where a.user.email = ?1 and a.date < ?2 order by a.date asc")
    List<Appointment> getPastAppointment(String email, LocalDateTime date);

    @Query("SELECT COUNT(a) from Appointment a where a.user.email = ?1 and a.date between ?2 and ?3")
    long getPatientAppointmentCount(String email, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(a) from Appointment a where a.user.email = ?1 and a.status = 2 and a.date between ?2 and ?3 " +
            "and a.date between ?2 and ?4")
    long getPatientNoshowCount(String email, LocalDateTime start, LocalDateTime current, LocalDateTime end);

    @Query("SELECT COUNT(a) from Appointment a where a.clinic.name = ?1 and a.date between ?2 and ?3")
    long getClinicAppointmentCount(String clinic, LocalDateTime start, LocalDateTime end);

    @Query("SELECT COUNT(a) from Appointment a where a.clinic.name = ?1 and a.status = 2 and a.date between ?2 and ?3 " +
            "and a.date between ?2 and ?4")
    long getClinicNoshowCount(String clinic, LocalDateTime start, LocalDateTime current, LocalDateTime end);
}
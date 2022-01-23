package cmpe275.vms.services;

import cmpe275.vms.models.Appointment;
import cmpe275.vms.models.Clinic;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    Appointment create(Appointment app);
    void delete(Appointment app);
    Appointment update(Appointment app);
    long getAppointmentCount(LocalDateTime date, Clinic clinic);
    Appointment findById(String id);
    List<Appointment> getFutureAppointment(String mrn, LocalDateTime date);
    List<Appointment> getPastAppointment(String mrn, LocalDateTime date);
    long getPatientAppointmentCount(String email, LocalDateTime start, LocalDateTime end);
    long getPatientNoshowCount(String email, LocalDateTime start, LocalDateTime current, LocalDateTime end);
    long getClinicAppointmentCount(String clinic, LocalDateTime start, LocalDateTime end);
    long getClinicNoshowCount(String clinic, LocalDateTime start, LocalDateTime current, LocalDateTime end);
}

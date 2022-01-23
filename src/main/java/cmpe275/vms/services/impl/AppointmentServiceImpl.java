package cmpe275.vms.services.impl;

import cmpe275.vms.exceptions.AttributeErrorException;
import cmpe275.vms.exceptions.RecordNotFoundException;
import cmpe275.vms.models.Appointment;
import cmpe275.vms.models.Clinic;
import cmpe275.vms.repositories.AppointmentRepository;
import cmpe275.vms.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;

    @Override
    public Appointment create(Appointment app) {
        try {
            appointmentRepository.save(app);
        } catch (Exception e) {
            throw new AttributeErrorException("one or more attribute are incorrect");
        }
        return app;
    }

    @Override
    public void delete(Appointment app) {
    }

    @Override
    public Appointment update(Appointment app) {
        return null;
    }

    @Override
    public long getAppointmentCount(LocalDateTime date, Clinic clinic) {
        return appointmentRepository.getAppointmentCount(date, clinic);
    }

    @Override
    public Appointment findById(String id) {
        Appointment app = appointmentRepository.findById(id).orElseThrow(
                () -> new RecordNotFoundException("Invalid Appointment Id"));
        return app;
    }

    @Override
    public List<Appointment> getFutureAppointment(String mrn, LocalDateTime date) {
        List<Appointment> a = appointmentRepository.getFutureAppointment(mrn, date);
        return a;
    }

    @Override
    public List<Appointment> getPastAppointment(String mrn, LocalDateTime date) {
        List<Appointment> a = appointmentRepository.getPastAppointment(mrn, date);
        return a;
    }

    @Override
    public long getPatientAppointmentCount(String email, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.getPatientAppointmentCount(email, start, end);
    }

    @Override
    public long getPatientNoshowCount(String email, LocalDateTime start, LocalDateTime current, LocalDateTime end) {
        return appointmentRepository.getPatientNoshowCount(email, start, current, end);
    }

    @Override
    public long getClinicAppointmentCount(String clinic, LocalDateTime start, LocalDateTime end) {
        return appointmentRepository.getClinicAppointmentCount(clinic, start, end);
    }

    @Override
    public long getClinicNoshowCount(String clinic, LocalDateTime start, LocalDateTime current, LocalDateTime end) {
        return appointmentRepository.getClinicNoshowCount(clinic, start, current, end);
    }
}

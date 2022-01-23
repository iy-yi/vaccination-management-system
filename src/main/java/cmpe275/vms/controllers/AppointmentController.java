package cmpe275.vms.controllers;

import cmpe275.vms.exceptions.AttributeErrorException;
import cmpe275.vms.models.*;
import cmpe275.vms.models.enums.Status;
import cmpe275.vms.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    final
    AppointmentService appointmentService;

    final
    ClinicService clinicService;

    final
    VaccineService vaccineService;

    final
    VaccinationEntryService vaccinationEntryService;

    final
    UserService userService;

    @Autowired
    EmailService emailService;

    public AppointmentController(AppointmentService appointmentService, ClinicService clinicService, VaccineService vaccineService, VaccinationEntryService vaccinationEntryService, UserService userService) {
        this.appointmentService = appointmentService;
        this.clinicService = clinicService;
        this.vaccineService = vaccineService;
        this.vaccinationEntryService = vaccinationEntryService;
        this.userService = userService;
    }

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Transactional(rollbackFor = Exception.class)
    public Appointment create(@RequestBody Appointment app, Authentication auth) {
        int hour = app.getDate().getHour();
        Clinic cl = clinicService.get(app.getClinic().getName());
        app.setClinic(cl);
        long count = appointmentService.getAppointmentCount(app.getDate(), cl);
        System.out.println("existed appointment count: " + count);
        List<Vaccination> vs = new ArrayList<>();
        for(Vaccination v : app.getVaccines()) {
            vs.add(vaccineService.get(v.getId()));
        }
        app.setVaccines(vs);

        User user = userService.findEmail(auth.getName());
        app.setUser(user);

        if(hour >= cl.getOpen() && hour < cl.getClose() && count + 1 <= cl.getNumberOfPhysicians()) {
            Appointment createApp = appointmentService.create(app);
            // add to user appointment list
            user.getAppointments().add(createApp);

            // add to user vaccine list
            for (Vaccination v: createApp.getVaccines()) {
                List<VaccinationEntry> vaccineDue = user.getVaccines()
                        .stream().filter(e -> !e.isComplete() && e.getVaccination().getId() == v.getId())
                        .collect(Collectors.toList());
                // first shot
                if(vaccineDue.isEmpty()) {
                    VaccinationEntry entry = new VaccinationEntry(
                            v, createApp, 1, createApp.getDate(), false);
                    vaccinationEntryService.create(entry);
                    user.getVaccines().add(entry);
                } else {
                    // not first shot, update vaccine record with appointment
                    VaccinationEntry vn = vaccineDue.get(0);
                    vn.setAppointment(createApp);
                }

            }
            userService.update(user);
            emailService.sendEmail(auth.getName(), "Appointment Created!", "You've created an appointment for your vaccine! See you there!");
            return createApp;
        } else {
            throw new AttributeErrorException("Hour not in range");
        }
    }

    @PostMapping("/checkin")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Transactional(rollbackFor = Exception.class)
    public void checkin(@RequestParam String id, Authentication auth
                        , @RequestParam("current")
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime current) {

        try {
            Appointment app = appointmentService.findById(id);
            Duration dur = Duration.between(current, app.getDate());
            // checkin within 24 hours
            if (dur.toHours() > 24 || dur.toHours() < -24) {
                throw new AttributeErrorException("Appointment checkin within 24 hours.");
            }
            app.setStatus(Status.CHECKED);
            User user = app.getUser();
            // update all vaccination entries associated to the appointment
            List<VaccinationEntry> vaccines = user.getVaccines()
                    .stream().filter(v -> v.getAppointment() != null)
                    .filter(v -> v.getAppointment().getId().equals(app.getId()))
                    .collect(Collectors.toList());
            for (VaccinationEntry vEntry: vaccines) {
                vEntry.setComplete(true);
                vEntry.setDue(app.getDate());
                Vaccination vaccine = vEntry.getVaccination();
                if (vEntry.getShot() < vaccine.getNumOfShots()) {
                    VaccinationEntry newEntry = new VaccinationEntry(vaccine, vEntry.getShot()+1, false);
                    int interval = vaccine.getShotInterval();
                    LocalDateTime dueDate = app.getDate().plusDays(interval);
                    newEntry.setDue(dueDate);
                    vaccinationEntryService.create(newEntry);
                    user.getVaccines().add(newEntry);
                } else if (vaccine.getDuration() > 0 ) {
                    // add record after duration
                    int duration = vaccine.getDuration();
                    VaccinationEntry nextShot = new VaccinationEntry(vaccine, 1, false);
                    LocalDateTime dueDate = app.getDate().plusDays(duration);
                    nextShot.setDue(dueDate);
                    vaccinationEntryService.create(nextShot);
                    user.getVaccines().add(nextShot);
                }
            }

            emailService.sendEmail(auth.getName(), "Appointment Checked in!", "You've checked in your appointment for the vaccine!");


        } catch (Exception e) {
            throw new AttributeErrorException("Appointment Checkin Fail");
        }
    }

    @PostMapping("/cancel")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Transactional(rollbackFor = Exception.class)
    public void cancel(@RequestParam String id, Authentication auth) {
        Appointment app = appointmentService.findById(id);
        try {
            app.setStatus(Status.CANCELED);
            User user = app.getUser();
            List<VaccinationEntry> vaccines = user.getVaccines()
                    .stream().filter(v -> v.getAppointment() != null)
                    .filter(v -> v.getAppointment().getId().equals(id))
                    .collect(Collectors.toList());
            for (VaccinationEntry vaccine: vaccines) {
                vaccine.setAppointment(null);
            }

            emailService.sendEmail(auth.getName(), "Appointment Canceled!", "You've canceled your appointment for the vaccine!");

        } catch (Exception e) {
            e.printStackTrace();
            throw new AttributeErrorException("Appointment Cancel Fail");
        }
    }

    @PostMapping("/update")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @Transactional(rollbackFor = Exception.class)
    public Appointment update(@RequestParam String id, @RequestParam String clinic, @RequestParam("localDateTime")
                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        try {
            Clinic cl = clinicService.get(clinic);
            int hour = date.getHour();
            long count = appointmentService.getAppointmentCount(date, cl);
            if(hour >= cl.getOpen() && hour < cl.getClose() && count + 1 <= cl.getNumberOfPhysicians()) {
                Appointment originalApp = appointmentService.findById(id);
                originalApp.setClinic(cl);
                originalApp.setDate(date);
                return originalApp;
            } else {
                throw new AttributeErrorException("The selected clinic is full at this time.");
            }
        } catch (Exception e) {
            throw new AttributeErrorException("Appointment update Fail");
        }
    }

    @GetMapping("/get")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Appointment get(@RequestParam String id, Authentication auth) {
        return appointmentService.findById(id);
    }


    @GetMapping("/future")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Appointment> futureList(@RequestParam("localDateTime")
                                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime, Authentication auth) {
        return appointmentService.getFutureAppointment(auth.getName(), localDateTime);
    }

    @GetMapping("/past")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Appointment> pastList(@RequestParam("localDateTime")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime, Authentication auth) {
        return appointmentService.getPastAppointment(auth.getName(), localDateTime);
    }

    @GetMapping("/patientreport")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public double[] patientReport(@RequestParam("currentDate")
                                      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime currentDate, Authentication auth
                        , @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate
                        , @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        double total = (double) appointmentService.getPatientAppointmentCount(auth.getName(), startDate, endDate);
        double noShow = (double) appointmentService.getPatientNoshowCount(auth.getName(), startDate, currentDate, endDate);
        double noShowRate = 0;
        if (total > 0) {
            noShowRate = noShow / total;
        }

        double[] data = {total, noShow, noShowRate};
        return data;
    }

    @GetMapping("/clinicreport")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Admin')")
    public double[] clinicReport(@RequestParam("currentDate")
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime currentDate, Authentication auth
            , @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate
            , @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate, @RequestParam("clinic") String clinic) {
        double total = (double) appointmentService.getClinicAppointmentCount(clinic, startDate, endDate);
        double noShow = (double) appointmentService.getClinicNoshowCount(clinic, startDate, currentDate, endDate);
        double noShowRate = 0;
        if (total > 0) {
            noShowRate = noShow / total;
        }

        double[] data = {total, noShow, noShowRate};
        return data;
    }
}

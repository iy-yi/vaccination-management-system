package cmpe275.vms.controllers;

import cmpe275.vms.models.User;
import cmpe275.vms.models.Vaccination;
import cmpe275.vms.models.VaccinationEntry;
import cmpe275.vms.services.UserService;
import cmpe275.vms.services.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/vaccine")
public class VaccineController {
    @Autowired
    VaccineService vaccineService;

    @Autowired
    UserService userService;

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Admin')")
    public Vaccination create(@RequestBody Vaccination vaccine, HttpServletRequest request) {
        Vaccination vs = vaccineService.create(vaccine);
        return vs;
    }

    @DeleteMapping("/delete/{name}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Admin')")
    public void delete(HttpServletRequest request, @PathVariable String name) {
        vaccineService.delete(name);
    }

    @GetMapping("/history")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<VaccinationEntry> getVaccineHistory(@RequestParam("localDateTime")
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime
                                , Authentication auth) {
        User user = userService.findEmail(auth.getName());
        List<VaccinationEntry> vaccinationHistory = user.getVaccines()
                .stream().filter(v -> v.isComplete() == true && v.getDue().isBefore(localDateTime))
                .collect(Collectors.toList());
        Collections.sort(vaccinationHistory);
        System.out.println(vaccinationHistory);
        return vaccinationHistory;
    }

    @GetMapping("/due")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<VaccinationEntry> getVaccineDue(@RequestParam("localDateTime")
                                                    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime
            , Authentication auth) {
        User user = userService.findEmail(auth.getName());
        LocalDateTime end = localDateTime.plusMonths(12);
        List<VaccinationEntry> vaccinationDue = user.getVaccines()
                .stream().filter(v -> v.getDue().isBefore(end) && (v.isComplete() == false || v.getDue().isAfter(localDateTime) ))
                .collect(Collectors.toList());
        Collections.sort(vaccinationDue);
        return vaccinationDue;
    }

    @GetMapping("/all")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<Vaccination> findAll() {
        return vaccineService.findAll();
    }
}

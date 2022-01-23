package cmpe275.vms.controllers;

import cmpe275.vms.exceptions.AttributeErrorException;
import cmpe275.vms.models.Clinic;
import cmpe275.vms.models.Disease;
import cmpe275.vms.repositories.ClinicRepository;
import cmpe275.vms.services.ClinicService;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/clinic")
public class ClinicController {

    @Autowired
    ClinicService clinicService;

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Clinic create(@RequestBody Clinic clinic, HttpServletRequest request) {
        if(clinic.getClose() - clinic.getOpen() < 8) {
            throw new AttributeErrorException("Operation hour not satisfied");
        }
        return clinicService.create(clinic);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Clinic> findAll(){
        return clinicService.findAll();
    }

    @DeleteMapping("/delete/{name}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Admin')")
    public void delete(HttpServletRequest request, @PathVariable String name) {
        clinicService.delete(name);
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public List<Clinic> findAvailable(@RequestParam("localDateTime")
                                          @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime){

        return clinicService.findAllAvailable(localDateTime, localDateTime.getHour());
    }
}

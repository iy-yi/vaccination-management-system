package cmpe275.vms.controllers;

import cmpe275.vms.models.Disease;
import cmpe275.vms.services.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/disease")
public class DiseasesController {
    @Autowired
    DiseaseService diseaseService;

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Admin')")
    public Disease create(@RequestBody Disease disease, HttpServletRequest request) {
        Disease ds = diseaseService.create(disease);
        return ds;
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Admin')")
    public Disease update(@RequestBody Disease disease, HttpServletRequest request) {
        return null;
    }

    @DeleteMapping("/delete/{name}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Admin')")
    public void delete(HttpServletRequest request, @PathVariable String name) {
        diseaseService.delete(name);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('Admin')")
    public List<Disease> findAll(){
        return diseaseService.findAll();
    }


}

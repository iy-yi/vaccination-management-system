package cmpe275.vms.services.impl;

import cmpe275.vms.exceptions.DuplicateEntryException;
import cmpe275.vms.exceptions.RecordNotFoundException;
import cmpe275.vms.models.Clinic;
import cmpe275.vms.repositories.ClinicRepository;
import cmpe275.vms.services.ClinicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClinicServiceImpl implements ClinicService {
    @Autowired
    ClinicRepository clinicRepository;

    @PostMapping("/create")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Clinic create(Clinic clinic) {
        Clinic c = clinicRepository.findById(clinic.getName()).orElse(null);
        if(c != null) {
                throw new DuplicateEntryException("Clinic name existed");
        }
        return clinicRepository.saveAndFlush(clinic);
    }

    @Override
    public Clinic update(Clinic clinic) {
        return null;
    }

    @Override
    public void delete(String name) {
        clinicRepository.deleteById(name);
    }

    @Override
    public Clinic get(String name) {
        return clinicRepository.findById(name).orElseThrow(() -> new RecordNotFoundException("clinic record not found"));
    }

    @Override
    public List<Clinic> findAll(){
        return clinicRepository.findAll();
    }

    @Override
    public List<Clinic> findAllAvailable(LocalDateTime time, int hour){
        List<Clinic> first = clinicRepository.getAvailableClinic(time, hour);
        List<Clinic> free = clinicRepository.getFreeClinic(time, hour);
        System.out.println(first);
        System.out.println(free);
        first.addAll(free);


        return first;
    }


}

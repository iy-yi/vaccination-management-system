package cmpe275.vms.services.impl;

import cmpe275.vms.exceptions.AttributeErrorException;
import cmpe275.vms.exceptions.DuplicateEntryException;
import cmpe275.vms.exceptions.RecordNotFoundException;
import cmpe275.vms.models.Vaccination;
import cmpe275.vms.repositories.VaccinationRepository;
import cmpe275.vms.services.VaccineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class VaccineServiceImpl implements VaccineService {
    @Autowired
    VaccinationRepository vaccinationRepository;

    @Override
    public Vaccination create(Vaccination vaccine) {
        if(vaccine.getName().length() < 3) {
            throw new AttributeErrorException("Name too short");
        } else {
            try {
                vaccinationRepository.saveAndFlush(vaccine);
            } catch(Exception e) {
                throw new DuplicateEntryException("Vaccine existed");
            }
        }
        return null;
    }

    @Override
    public void delete(String name) {
        try{
            vaccinationRepository.deleteByName(name);
        } catch(Exception e) {
            throw new RecordNotFoundException("vaccine does not exist");
        }

    }

    @Override
    public Vaccination update(Vaccination vaccine) {
        return null;
    }

    @Override
    public Vaccination get(String name) {
        Vaccination vs = vaccinationRepository.findByName(name);
        if(vs == null) {
            throw new RecordNotFoundException("vaccine with name: " + name + " not found");
        }
        return vs;
    }

    @Override
    public Vaccination get(long id) {
        Vaccination vs = vaccinationRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("vaccine with name: " + id + " not found"));

        return vs;
    }

    @Override
    public List<Vaccination> findAll() {
        return vaccinationRepository.findAll();
    }
}

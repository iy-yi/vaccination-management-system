package cmpe275.vms.services.impl;

import cmpe275.vms.exceptions.RecordNotFoundException;
import cmpe275.vms.models.Vaccination;
import cmpe275.vms.models.VaccinationEntry;
import cmpe275.vms.repositories.VaccinationEntryRepository;
import cmpe275.vms.repositories.VaccinationRepository;
import cmpe275.vms.services.VaccinationEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class VaccinationEntryServiceImpl implements VaccinationEntryService {
    @Autowired
    VaccinationEntryRepository vaccinationEntryRepository;

    @Override
    public VaccinationEntry create (VaccinationEntry vaccinationEntry) {
        return vaccinationEntryRepository.saveAndFlush(vaccinationEntry);
    }
}

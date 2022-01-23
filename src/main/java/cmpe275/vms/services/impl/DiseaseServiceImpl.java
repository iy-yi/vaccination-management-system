package cmpe275.vms.services.impl;

import cmpe275.vms.exceptions.DuplicateEntryException;
import cmpe275.vms.models.Disease;
import cmpe275.vms.repositories.DiseaseRepository;
import cmpe275.vms.services.DiseaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class DiseaseServiceImpl implements DiseaseService {

    @Autowired
    DiseaseRepository diseaseRepository;

    @Override
    public Disease create(Disease disease) {
        if(diseaseRepository.findByName(disease.getName()) != null) {
            throw new DuplicateEntryException("Disease existed");
        }

        return diseaseRepository.saveAndFlush(disease);
    }

    @Override
    public Disease update(Disease disease) {
        Disease cur = diseaseRepository.getOne(disease.getId());
        cur.setDescription(disease.getDescription());
        cur.setName(disease.getName());
        diseaseRepository.saveAndFlush(cur);
        return disease;
    }

    @Override
    public void delete(String diseaseName) {
        diseaseRepository.deleteByName(diseaseName);
    }

    @Override
    public List<Disease> findAll(){
        return diseaseRepository.findAll();
    }
}

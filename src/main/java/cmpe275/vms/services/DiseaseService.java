package cmpe275.vms.services;

import cmpe275.vms.models.Disease;

import java.util.List;

public interface DiseaseService {
    Disease create(Disease disease);
    Disease update(Disease disease);
    void delete(String diseaseName);
    List<Disease> findAll();
}

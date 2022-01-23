package cmpe275.vms.services;

import cmpe275.vms.models.Vaccination;

import java.util.List;

public interface VaccineService {
    Vaccination create(Vaccination vaccine);
    void delete(String name);
    Vaccination update(Vaccination vaccine);
    Vaccination get(String name);
    Vaccination get(long id);
    List<Vaccination> findAll();
}

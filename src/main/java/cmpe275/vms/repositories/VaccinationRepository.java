package cmpe275.vms.repositories;

import cmpe275.vms.models.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {
    void deleteByName(String name);
    Vaccination findByName(String name);
}
package cmpe275.vms.repositories;

import cmpe275.vms.models.VaccinationEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccinationEntryRepository extends JpaRepository<VaccinationEntry, String> {

}